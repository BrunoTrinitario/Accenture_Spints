package com.mindhub.order_service.dtos;

import com.mindhub.order_service.models.OrderEntity;
import com.mindhub.order_service.models.OrderStatus;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Long userId;
    private List<OrderItemRecord> orderItemRecordList;
    private OrderStatus orderStatus;

    public OrderDTO(OrderEntity order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.orderStatus = order.getOrderStatus();
        if (order.getOrderItemList()!=null)
            this.orderItemRecordList = order.getOrderItemList().stream().map(orderItem ->  new OrderItemRecord(orderItem.getId(),orderItem.getProductId(),orderItem.getQuantity())).toList();
    }

    public OrderDTO(Long id, Long userId, OrderStatus orderStatus, List<OrderItemRecord> orderItemRecordList) {
        this.id = id;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.orderItemRecordList = orderItemRecordList;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<OrderItemRecord> getOrderItemRecordList() {
        return orderItemRecordList;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
