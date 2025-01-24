package com.mindhub.order_service.controllers;

import com.mindhub.order_service.dtos.NewOrderRecord;
import com.mindhub.order_service.dtos.OrderCreatedRecord;
import com.mindhub.order_service.dtos.OrderDTO;
import com.mindhub.order_service.dtos.UpdateOrderRecord;
import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.models.OrderStatus;
import com.mindhub.order_service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/API/orders")
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * Retrieve all orders.
     * @return A set of {@link OrderDTO} objects.
     * @response 200 OK - List of all orders.
     */
    @GetMapping
    public ResponseEntity<Set<OrderDTO>> getAllOrders() {
        Set<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieve all orders of a specific user.
     * @param userId The ID of the user.
     * @return A set of {@link OrderDTO} objects associated with the user.
     * @response 200 OK - List of the user's orders.
     */
    @GetMapping("/all/{userId}")
    public ResponseEntity<Set<OrderDTO>> getAllOrdersByUserId(@PathVariable Long userId) {
        Set<OrderDTO> orders = orderService.getAllOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }


    /**
     * Retrieve an order by its ID.
     * @param orderId The ID of the order.
     * @return The {@link OrderDTO} object corresponding to the provided ID.
     * @throws OrderException If the order does not exist.
     * @response 200 OK - Order details.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) throws OrderException {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Create a new order.
     * @param newOrder A {@link NewOrderRecord} object containing the details of the new order.
     * @return The created order as a {@link OrderDTO}.
     * @throws OrderException If an error occurs during creation or the input data is not valid.
     * @response 201 Created - Order successfully created.
     */

    @PostMapping
    public ResponseEntity<OrderCreatedRecord> createOrder(@RequestBody NewOrderRecord newOrder) throws OrderException {
        OrderCreatedRecord createdOrder = orderService.createOrder(newOrder);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Update the status of an order.
     * @param orderId The ID of the order.
     * @param updateOrderRecord An object containing the new order status.
     * @return The updated order as a {@link OrderDTO}.
     * @throws OrderException If the order does not exist or the status is invalid.
     * @response 200 OK - Order successfully updated.
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> changeStatus(@PathVariable Long orderId, @RequestBody UpdateOrderRecord updateOrderRecord) throws OrderException {
        OrderDTO orderDTO = orderService.changeStatus(orderId, updateOrderRecord.orderStatus());
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    /**
     * Delete an order by its ID.
     * @param orderId The ID of the order.
     * @throws OrderException If the order does not exist.
     * @response 204 No Content - Order successfully deleted.
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) throws OrderException {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
