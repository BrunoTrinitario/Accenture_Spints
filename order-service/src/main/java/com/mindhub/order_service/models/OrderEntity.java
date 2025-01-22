package com.mindhub.order_service.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    private Long userId;

    private OrderStatus orderStatus;

    public OrderEntity() {
    }

    public OrderEntity(List<OrderItem> orderItemList, Long userId, OrderStatus orderStatus) {
        this.orderItemList = orderItemList;
        this.userId = userId;
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItemList.add(orderItem);
        orderItem.setOrder(this);
    }
}
