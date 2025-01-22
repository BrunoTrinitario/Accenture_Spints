package com.mindhub.order_service.services;

import com.mindhub.order_service.dtos.NewOrderRecord;
import com.mindhub.order_service.dtos.OrderDTO;
import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import com.mindhub.order_service.models.OrderEntity;
import com.mindhub.order_service.models.OrderItem;
import com.mindhub.order_service.models.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface OrderService {
    Set<OrderDTO> getAllOrders();
    Set<OrderDTO> getAllOrdersByUserId(Long id);
    OrderDTO getOrderById(Long id) throws OrderException;
    OrderDTO createOrder(NewOrderRecord newOrder) throws OrderException;
    OrderDTO changeStatus(Long id, OrderStatus orderStatus) throws OrderException;
    void deleteOrder(Long id) throws OrderException;
    boolean existsOrder(Long id);

}
