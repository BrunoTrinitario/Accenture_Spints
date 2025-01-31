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
    Set<OrderItemRecord> getAllOrderItemsByOrderId(Long userId, Long orderId) throws OrderException;
    OrderItemRecord addOrderItem(Long userId, Long orderId, ProductQuantityRecord productQuantityRecord) throws OrderException, OrderItemException;
    void deleteOrderItem(Long userId, Long orderItemId) throws OrderException, OrderItemException;
    OrderItemRecord updateOrderItemQuantity(Long userId, Long orderItemId, Integer quantity) throws OrderItemException, OrderException;
    boolean existsOrderItem(Long id);
}
