package com.mindhub.order_service.services.imp;

import com.mindhub.order_service.dtos.*;
import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import com.mindhub.order_service.exceptions.ProductServiceException;
import com.mindhub.order_service.models.OrderEntity;
import com.mindhub.order_service.models.OrderItem;
import com.mindhub.order_service.models.OrderStatus;
import com.mindhub.order_service.models.ProductError;
import com.mindhub.order_service.repositories.OrderItemRepository;
import com.mindhub.order_service.repositories.OrderRepository;
import com.mindhub.order_service.services.OrderItemService;
import com.mindhub.order_service.services.OrderService;
import com.mindhub.order_service.util.Constants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService, OrderItemService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${USERS_PATH}")
    private String userPath;

    @Value("${PRODUCTS_PATH}")
    private String productPath;

    @Override
    public Set<OrderDTO> getAllOrders() {
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        Set<OrderDTO> orderDTOSet = orderEntityList.stream().map(orderDto -> new OrderDTO(orderDto)).collect(Collectors.toSet());
        return  orderDTOSet;
    }

    @Override
    public Set<OrderDTO> getAllOrdersByUserId(Long id) {
        List<OrderEntity> orderEntityList = orderRepository.findByUserId(id);
        Set<OrderDTO> orderDTOSet = orderEntityList.stream().map(orderDto -> new OrderDTO(orderDto)).collect(Collectors.toSet());
        return  orderDTOSet;
    }

    @Override
    public OrderDTO getOrderById(Long id) throws OrderException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        OrderDTO orderDTO = new OrderDTO(order);
        return orderDTO;
    }

    @Override
    public OrderDTO getOrderByUserId(Long userId, Long orderId) throws OrderException {
        OrderDTO order = getOrderById(orderId);
        validateOrderOwner(userId,order.getUserId());
        return order;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public OrderCreatedRecord createOrder(String email, NewOrderRecord newOrder) throws OrderException {
            Long userId = getUserIdFromEmail(email);

            HashMap<Long,Integer> existentProductMap = getExistentProducts(newOrder.recordList());

            OrderEntity order = new OrderEntity(null, userId, OrderStatus.PENDING);

            orderRepository.save(order);

            List<ErrorProductRecord> orderItemsError = setOrderItemList(existentProductMap, newOrder.recordList(), order);

            orderRepository.save(order);

            try{
                updateProducts(order.getOrderItemList(),-1);
            }catch (ProductServiceException e){
                throw new OrderException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
            }

            OrderDTO orderDTO = new OrderDTO(order);
            OrderCreatedRecord orderCreatedRecord = new OrderCreatedRecord(orderDTO, orderItemsError);

            return orderCreatedRecord;

    }

    private HashMap<Long,Integer> getExistentProducts(List<ProductQuantityRecord> productQuantityRecordList) throws OrderException {
        ParameterizedTypeReference<HashMap<Long, Integer>> responseType =
                new ParameterizedTypeReference<>() {};
        HttpEntity<List<ProductQuantityRecord>> httpEntity = new HttpEntity<>(productQuantityRecordList);
        try{
            ResponseEntity<HashMap<Long, Integer>> responseEntity = restTemplate.exchange(productPath + "/private", HttpMethod.PUT ,httpEntity, responseType);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new OrderException(Constants.COM_ERR_PROD, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private List<ErrorProductRecord> setOrderItemList(HashMap<Long, Integer> existentProducts, List<ProductQuantityRecord> wantedProducts, OrderEntity order){
        List<OrderItem> orderItemList = new ArrayList<>();
        List<ErrorProductRecord> errorProductList = new ArrayList<>();
        wantedProducts.forEach(wantedProduct -> {
            if (existentProducts.containsKey(wantedProduct.id())){
               Integer realQuantity = existentProducts.get(wantedProduct.id());
               if (realQuantity>= wantedProduct.quantity()){
                   OrderItem orderItem = new OrderItem(wantedProduct.quantity(), order, wantedProduct.id());
                   orderItemRepository.save(orderItem);
                   orderItemList.add(orderItem);
               }else{
                   errorProductList.add(new ErrorProductRecord(wantedProduct.id(), ProductError.NO_STOCK));
               }
            }else{
                errorProductList.add(new ErrorProductRecord(wantedProduct.id(), ProductError.NOT_FOUND));
            }
        });

        order.setOrderItemList(orderItemList);

        return errorProductList;
    }

    private void updateProducts(List<OrderItem> orderItemList, int factor) throws ProductServiceException {

        List<ProductQuantityRecord> productQuantityRecordList = new ArrayList<>();
        orderItemList.forEach(orderItem -> {
            productQuantityRecordList.add(new ProductQuantityRecord(orderItem.getProductId(), factor*orderItem.getQuantity()));
        });

        HttpEntity<List<ProductQuantityRecord>> httpEntity = new HttpEntity<>(productQuantityRecordList);

        try{
            restTemplate.exchange(productPath + "/private/to-order", HttpMethod.PUT ,httpEntity, String.class);
        } catch (RestClientException e) {
            throw new ProductServiceException(Constants.COM_ERR_PROD);
        }

    }

    private Long getUserIdFromEmail(String email) throws OrderException {
        try{
            String url = userPath + "/private/email/" + email;
            Long userId = restTemplate.getForObject(url, Long.class);
            return userId;
        } catch (RestClientException e) {
            if (e instanceof HttpStatusCodeException){
                HttpStatusCodeException aux = (HttpStatusCodeException)e;
                throw new OrderException(Constants.USER_NOT_FOUND, (HttpStatus) aux.getStatusCode());
            }else{
                throw new OrderException(Constants.COM_USR_PROD, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public OrderDTO changeStatus(Long userId,String userMail, Long orderId, OrderStatus orderStatus) throws OrderException {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        validateOrderOwner(userId,order.getUserId());
        order.setOrderStatus(orderStatus);
        order = orderRepository.save(order);
        if (order.getOrderStatus() == OrderStatus.COMPLETED){
            sendDataToGeneratePdf(order,userMail);
        }
        return new OrderDTO(order);
    }

    private void sendDataToGeneratePdf(OrderEntity order,String userMail){
        List<ProductRecord> listProducts = new ArrayList<>();
        Iterator<OrderItem> it = order.getOrderItemList().iterator();
        while (it.hasNext()){
            OrderItem aux = it.next();
            ProductRecord product = restTemplate.getForObject(productPath + "/admin/" + aux.getProductId(), ProductRecord.class );
            listProducts.add(product);
        }
        OrderToPdfDTO orderToPdfDTO = new OrderToPdfDTO(order.getId(), order.getUserId(), userMail, listProducts);
        rabbitTemplate.convertAndSend("email-exchange", "user.pdf", orderToPdfDTO);
    }

    @Override
    public void deleteOrder(Long id) throws OrderException {
        if (existsOrder(id)){
            orderRepository.deleteById(id);
        }else{
            throw new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteOrderUser(Long userId, Long orderId) throws OrderException {
        OrderDTO order = getOrderById(orderId);
        validateOrderOwner(userId,order.getUserId());
        deleteOrder(orderId);
    }

    @Override
    public boolean existsOrder(Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public Set<OrderItemRecord> getAllOrderItemsByOrderId(Long userId, Long orderId) throws OrderException {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        validateOrderOwner(userId,order.getUserId());
        Set<OrderItemRecord> orderItemSet = order.getOrderItemList().stream().map(orderItem -> new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity())).collect(Collectors.toSet());
        return orderItemSet;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public OrderItemRecord addOrderItem(Long userId, Long OrderId, ProductQuantityRecord productQuantityRecord) throws OrderException, OrderItemException {
        OrderEntity order = orderRepository.findById(OrderId).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        validateOrderOwner(userId,order.getUserId());
        validOrderStatus(order.getId());
        validateOrderItem(userId, order.getId(), productQuantityRecord.id());
        if (productQuantityRecord.quantity()<0){
            throw new OrderItemException(Constants.INV_QUANTITY);
        }

        List<ProductQuantityRecord> auxList = new ArrayList<>();
        auxList.add(productQuantityRecord);

        HashMap<Long, Integer> existentProductMap = getExistentProducts(auxList);

        if (existentProductMap.containsKey(productQuantityRecord.id())){
            Integer realQuantity = existentProductMap.get(productQuantityRecord.id());
            if (realQuantity>= productQuantityRecord.quantity()){

                OrderItem orderItem = new OrderItem(productQuantityRecord.quantity(), order, productQuantityRecord.id());

                List<OrderItem> orderItemList = new ArrayList<>();
                orderItemList.add(orderItem);
                try{
                    updateProducts(orderItemList,-1);
                }catch (ProductServiceException e){
                    throw new OrderException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
                }

                orderItemRepository.save(orderItem);
                order.addOrderItem(orderItem);
                orderRepository.save(order);

                return new OrderItemRecord(order.getId(), orderItem.getProductId(), orderItem.getQuantity());
            }else{
                throw new OrderException(Constants.NEGATIVE_STOCK, HttpStatus.NOT_FOUND);
            }
        }else{
            throw new OrderException(Constants.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }

    private void validateOrderItem(Long userId, Long orderId,Long orderItemProductId) throws OrderException {
        Set<OrderItemRecord> orderItemSet = getAllOrderItemsByOrderId(userId,orderId);
        Iterator<OrderItemRecord> it = orderItemSet.iterator();
        while (it.hasNext()){
            if (it.next().productId()==orderItemProductId){
                throw new OrderException(Constants.ITEM_ALREADY_EXISTS);
            }
        }
    }

    @Override
    public void deleteOrderItem(Long userId, Long orderItemId) throws OrderItemException, OrderException {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(()->new OrderItemException(Constants.ORDER_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND));
        validateOrderOwner(userId, orderItem.getOrder().getUserId());

        validOrderStatus(orderItem.getOrder().getId());

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        updateProducts(orderItemList,1);

        orderItemRepository.delete(orderItem);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public OrderItemRecord updateOrderItemQuantity(Long userId,Long orderItemId, Integer quantity) throws OrderItemException, OrderException {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(()->new OrderItemException(Constants.ORDER_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND));
        validateOrderOwner(userId, orderItem.getOrder().getUserId());
        validOrderStatus(orderItem.getOrder().getId());

        int diference = orderItem.getQuantity()-quantity;

        if (quantity>0 && orderItem.getQuantity() != quantity){

            HashMap<Long, Integer> existentProduct = getExistentProducts(List.of(new ProductQuantityRecord(orderItem.getProductId(),quantity)));

            try{
                if (diference>0) {
                    updateProducts(List.of(new OrderItem(diference, null, orderItem.getProductId())), 1);
                }else {
                    if (existentProduct.get(orderItem.getProductId())>= -1*diference){
                        updateProducts(List.of(new OrderItem(diference, null, orderItem.getProductId())), 1);
                    }else{
                        throw new OrderException(Constants.NEGATIVE_STOCK, HttpStatus.NOT_ACCEPTABLE);
                    }
                }
            }catch (ProductServiceException e){
                throw new OrderException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            orderItem.setQuantity(quantity);
            orderItem = orderItemRepository.save(orderItem);
            return new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity());

        }else{
            throw new OrderException(Constants.INV_QUANTITY, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @Override
    public boolean existsOrderItem(Long id) {
        return orderItemRepository.existsById(id);
    }

    private void validOrderStatus(Long id) throws OrderException {
        OrderDTO order = getOrderById(id);
        if (order.getOrderStatus()==OrderStatus.COMPLETED){
            throw new OrderException(Constants.ORDER_COMPLETED,HttpStatus.UNAUTHORIZED);
        }
    }

    private void validateOrderOwner(Long userId, Long orderId) throws OrderException {
        if (userId!=orderId){
            throw new OrderException(Constants.NOT_PERM, HttpStatus.UNAUTHORIZED);
        }
    }
}
