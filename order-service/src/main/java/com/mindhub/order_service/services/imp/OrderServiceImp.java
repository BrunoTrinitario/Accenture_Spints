package com.mindhub.order_service.services.imp;

import com.mindhub.order_service.dtos.*;
import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import com.mindhub.order_service.models.OrderEntity;
import com.mindhub.order_service.models.OrderItem;
import com.mindhub.order_service.models.OrderStatus;
import com.mindhub.order_service.models.ProductError;
import com.mindhub.order_service.repositories.OrderItemRepository;
import com.mindhub.order_service.repositories.OrderRepository;
import com.mindhub.order_service.services.OrderItemService;
import com.mindhub.order_service.services.OrderService;
import com.mindhub.order_service.util.Constants;
import com.mindhub.order_service.util.RestTemplateConfig;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceImp implements OrderService, OrderItemService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private RestTemplate restTemplate;

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
    public OrderCreatedRecord createOrder(NewOrderRecord newOrder) throws OrderException {

            Long userId = getUserIdFromEmail(newOrder.email());

            ParameterizedTypeReference<List<ExistentProductsRecord>> responseType =
                    new ParameterizedTypeReference<>() {};
            HttpEntity<List<ProductQuantityRecord>> httpEntity = new HttpEntity<>(newOrder.recordList());

            try{
                ResponseEntity<List<ExistentProductsRecord>> responseEntity = restTemplate.exchange(productPath, HttpMethod.PUT ,httpEntity, responseType);
                OrderEntity order = new OrderEntity(null, userId, OrderStatus.PENDING);
                orderRepository.save(order);
                generateOrderItemList(responseEntity.getBody(), order);
                orderRepository.save(order);

                List<ErrorProductRecord> errorList = generateErrorProductList(newOrder.recordList(), responseEntity.getBody());

                OrderDTO orderDTO = new OrderDTO(order);

                OrderCreatedRecord orderCreatedRecord = new OrderCreatedRecord(orderDTO, errorList);

                return orderCreatedRecord;
            }catch (HttpClientErrorException e){
                throw new OrderException(Constants.COM_ERR_PROD, HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    private Long getUserIdFromEmail(String email) throws OrderException {
        try{
            String url = userPath + "/email/" + email;
            Long userId = restTemplate.getForObject(url, Long.class);
            return userId;
        } catch (RestClientException e) {
            if (e instanceof HttpStatusCodeException){
                HttpStatusCodeException aux = (HttpStatusCodeException)e;
                throw new OrderException(Constants.COM_ERR_PROD, (HttpStatus) aux.getStatusCode());
            }else{
                throw new OrderException(Constants.COM_USR_PROD, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
    }

    private List<ErrorProductRecord> generateErrorProductList(List<ProductQuantityRecord> userProductsList,List<ExistentProductsRecord> existentProductsList){
        List<ErrorProductRecord> errorProductList = new ArrayList<>();
        List<ProductQuantityRecord> aux = userProductsList.stream()
                .filter(userProduct ->
                        !existentProductsList.stream().anyMatch(availableProduct ->
                                availableProduct.id().equals(userProduct.id()) && availableProduct.price() != null)
                ).toList();

        aux.forEach(product -> {
            boolean productExists = existentProductsList.stream()
                        .anyMatch(p -> p.id().equals(product.id()));
               if (productExists) {
                   errorProductList.add(new ErrorProductRecord(product.id(), ProductError.NO_STOCK));
               } else {
                   errorProductList.add(new ErrorProductRecord(product.id(), ProductError.NOT_FOUND));
               }
        });

        return  errorProductList;
    }

    private void generateOrderItemList(List<ExistentProductsRecord> productQuantityList, OrderEntity order){
        List<OrderItem> orderItemList = new ArrayList<>();
        Iterator<ExistentProductsRecord> it = productQuantityList.iterator();
        while (it.hasNext()){
            ExistentProductsRecord aux = it.next();
            if (aux.price()!=null){
                OrderItem orderItem = new OrderItem(aux.quantity(),order, aux.id());
                orderItemRepository.save(orderItem);
                orderItemList.add(orderItem);
            }
        }
        order.setOrderItemList(orderItemList);
    }

    @Override
    public OrderDTO changeStatus(Long id, OrderStatus orderStatus) throws OrderException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        order.setOrderStatus(orderStatus);
        order = orderRepository.save(order);
        return new OrderDTO(order);
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
    public boolean existsOrder(Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public Set<OrderItemRecord> getAllOrderItemsByOrderId(Long id) throws OrderException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        Set<OrderItemRecord> orderItemSet = order.getOrderItemList().stream().map(orderItem -> new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity())).collect(Collectors.toSet());
        return orderItemSet;
    }

    @Override
    public OrderItemRecord addOrderItem(NewOrderItemRecord newOrderItem) throws OrderException, OrderItemException {
        OrderEntity order = orderRepository.findById(newOrderItem.orderId()).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND, HttpStatus.NOT_FOUND));
        validateOrderItem(newOrderItem.orderId(),newOrderItem.productId());
        if (newOrderItem.quantity()<0 || newOrderItem.quantity()==null){
            throw new OrderItemException(Constants.INV_QUANTITY);
        }
        OrderItem orderItem = new OrderItem(newOrderItem.quantity(), order, newOrderItem.productId());
        order.addOrderItem(orderItem);
        order = orderRepository.save(order);
        return new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity());
    }

    private void validateOrderItem(Long orderId,Long orderItemProductId) throws OrderException {
        Set<OrderItemRecord> orderItemSet = getAllOrderItemsByOrderId(orderId);
        Iterator<OrderItemRecord> it = orderItemSet.iterator();
        while (it.hasNext()){
            if (it.next().productId()==orderItemProductId){
                throw new OrderException(Constants.ITEM_ALREADY_EXISTS);
            }
        }
    }

    @Override
    public void deleteOrderItem(Long id) throws OrderItemException {
        if (existsOrderItem(id)){
            orderItemRepository.deleteById(id);
        }else{
            throw new OrderItemException(Constants.ORDER_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public OrderItemRecord updateOrderItem(Long id, Integer quantity) throws OrderItemException {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(()->new OrderItemException(Constants.ORDER_ITEM_NOT_FOUND, HttpStatus.NOT_FOUND));
        orderItem.setQuantity(quantity);
        orderItem = orderItemRepository.save(orderItem);
        return new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity());
    }

    @Override
    public boolean existsOrderItem(Long id) {
        return orderItemRepository.existsById(id);
    }
}
