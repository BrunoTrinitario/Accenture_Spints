package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.OrderStatus;

import java.util.List;

public class OrderDTO {
    private Long id;
    private Long userId;
    private List<OrderItemRecord> orderItemRecordList;
    private OrderStatus orderStatus;

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
