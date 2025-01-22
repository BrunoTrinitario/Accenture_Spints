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

    /**
     * Retrieve all order items by order ID.
     * @param orderId The ID of the order.
     * @return A set of {@link OrderItemRecord} objects associated with the order.
     * @throws OrderException If the order does not exist.
     * @response 200 OK - List of order items for the specified order.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Set<OrderItemRecord>> getAllOrderItemsByOrderId(@PathVariable Long orderId) throws OrderException {
        Set<OrderItemRecord> orderItems = orderItemService.getAllOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    /**
     * Add a new order item to an order.
     * @param newOrderItem The {@link NewOrderItemRecord} object containing details of the new order item.
     * @throws OrderException If the order does not exist.
     * @throws OrderItemException If there is an issue with the new order item (e.g., invalid product ID).
     * @response 201 Created - Order item successfully added.
     */
    @PostMapping
    public ResponseEntity<Void> addOrderItem(@RequestBody NewOrderItemRecord newOrderItem) throws OrderException, OrderItemException {
        orderItemService.addOrderItem(newOrderItem);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Delete an order item by its ID.
     * @param orderItemId The ID of the order item to delete.
     * @throws OrderException If the associated order does not exist.
     * @throws OrderItemException If the order item does not exist.
     * @response 204 No Content - Order item successfully deleted.
     */
    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) throws OrderException, OrderItemException {
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update an order item's quantity.
     * @param orderItemId The ID of the order item to update.
     * @param updateOrderItemRecord An object containing the updated quantity for the order item.
     * @return The updated {@link OrderItemRecord}.
     * @throws OrderItemException If the order item does not exist or the quantity is invalid.
     * @response 200 OK - Order item successfully updated.
     */
    @PutMapping("/{orderItemId}")
    public ResponseEntity<OrderItemRecord> updateOrderItem(@PathVariable Long orderItemId, @RequestBody UpdateOrderItemRecord updateOrderItemRecord) throws OrderItemException {
        OrderItemRecord orderItems = orderItemService.updateOrderItem(orderItemId, updateOrderItemRecord.quantity());
        return ResponseEntity.ok(orderItems);
    }
}
