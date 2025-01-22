package com.mindhub.order_service.dtos;

import com.mindhub.order_service.models.OrderItem;
import com.mindhub.order_service.models.OrderStatus;

import java.util.List;

public record NewOrderRecord(Long userId, List<OrderItem> orderItemList, OrderStatus orderStatus) {
}
