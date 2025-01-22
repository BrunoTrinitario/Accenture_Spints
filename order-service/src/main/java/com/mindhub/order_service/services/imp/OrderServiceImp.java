package com.mindhub.order_service.services.imp;

import com.mindhub.order_service.dtos.*;
import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import com.mindhub.order_service.models.OrderEntity;
import com.mindhub.order_service.models.OrderItem;
import com.mindhub.order_service.models.OrderStatus;
import com.mindhub.order_service.repositories.OrderItemRepository;
import com.mindhub.order_service.repositories.OrderRepository;
import com.mindhub.order_service.services.OrderItemService;
import com.mindhub.order_service.services.OrderService;
import com.mindhub.order_service.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService, OrderItemService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

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
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND));
        OrderDTO orderDTO = new OrderDTO(order);
        return orderDTO;
    }

    @Override
    public OrderDTO createOrder(NewOrderRecord newOrder) throws OrderException {
        validateNewOrder(newOrder);
        OrderEntity order = new OrderEntity(newOrder.orderItemList(), newOrder.userId(), newOrder.orderStatus());
        order = orderRepository.save(order);
        OrderDTO orderDTO = new OrderDTO(order);
        return orderDTO;
    }

    private void validateNewOrder(NewOrderRecord newOrder) throws OrderException {
        if (newOrder.userId()==null || newOrder.orderStatus()==null){
            throw new OrderException(Constants.INVALID_ORDER);
        }
    }

    @Override
    public OrderDTO changeStatus(Long id, OrderStatus orderStatus) throws OrderException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND));
        order.setOrderStatus(orderStatus);
        order = orderRepository.save(order);
        return new OrderDTO(order);
    }

    @Override
    public void deleteOrder(Long id) throws OrderException {
        if (existsOrder(id)){
            orderRepository.deleteById(id);
        }else{
            throw new OrderException(Constants.ORDER_NOT_FOUND);
        }
    }

    @Override
    public boolean existsOrder(Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public Set<OrderItemRecord> getAllOrderItemsByOrderId(Long id) throws OrderException {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND));
        Set<OrderItemRecord> orderItemSet = order.getOrderItemList().stream().map(orderItem -> new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity())).collect(Collectors.toSet());
        return orderItemSet;
    }

    @Override
    public OrderItemRecord addOrderItem(NewOrderItemRecord newOrderItem) throws OrderException, OrderItemException {
        OrderEntity order = orderRepository.findById(newOrderItem.orderId()).orElseThrow(() -> new OrderException(Constants.ORDER_NOT_FOUND));
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
            throw new OrderItemException(Constants.ORDER_ITEM_NOT_FOUND);
        }

    }

    @Override
    public OrderItemRecord updateOrderItem(Long id, Integer quantity) throws OrderItemException {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(()->new OrderItemException(Constants.ORDER_ITEM_NOT_FOUND));
        orderItem.setQuantity(quantity);
        orderItem = orderItemRepository.save(orderItem);
        return new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity());
    }

    @Override
    public boolean existsOrderItem(Long id) {
        return orderItemRepository.existsById(id);
    }
}
