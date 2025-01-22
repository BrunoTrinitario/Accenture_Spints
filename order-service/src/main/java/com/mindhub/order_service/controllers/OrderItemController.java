package com.mindhub.order_service.controllers;

import com.mindhub.order_service.dtos.NewOrderItemRecord;
import com.mindhub.order_service.dtos.OrderItemRecord;
import com.mindhub.order_service.dtos.UpdateOrderItemRecord;
import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import com.mindhub.order_service.models.OrderItem;
import com.mindhub.order_service.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/API/order-items")
public class OrderItemController {
    @Autowired
    OrderItemService orderItemService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Set<OrderItemRecord>> getAllOrderItemsByOrderId(@PathVariable Long orderId) throws OrderException {
        Set<OrderItemRecord> orderItems = orderItemService.getAllOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @PostMapping
    public ResponseEntity<Void> addOrderItem(@RequestBody NewOrderItemRecord newOrderItem) throws OrderException, OrderItemException {
        orderItemService.addOrderItem(newOrderItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) throws OrderException, OrderItemException {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<OrderItemRecord> updateOrderItem(@PathVariable Long orderItemId, @RequestBody UpdateOrderItemRecord updateOrderItemRecord) throws OrderItemException {
        OrderItemRecord orderItems = orderItemService.updateOrderItem(orderItemId, updateOrderItemRecord.quantity());
        return ResponseEntity.ok(orderItems);
    }
}
