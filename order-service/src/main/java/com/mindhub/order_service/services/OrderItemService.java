package com.mindhub.order_service.services;

import com.mindhub.order_service.dtos.NewOrderItemRecord;
import com.mindhub.order_service.dtos.OrderItemRecord;
import com.mindhub.order_service.dtos.ProductQuantityRecord;
import com.mindhub.order_service.dtos.UpdateOrderItemRecord;
import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import com.mindhub.order_service.models.OrderItem;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface OrderItemService {
    Set<OrderItemRecord> getAllOrderItemsByOrderId(Long id) throws OrderException;
    OrderItemRecord addOrderItem(Long orderId, ProductQuantityRecord productQuantityRecord) throws OrderException, OrderItemException;
    void deleteOrderItem(Long id) throws OrderException, OrderItemException;
    OrderItemRecord updateOrderItem(Long id, Integer quantity) throws OrderItemException, OrderException;
    boolean existsOrderItem(Long id);
}
