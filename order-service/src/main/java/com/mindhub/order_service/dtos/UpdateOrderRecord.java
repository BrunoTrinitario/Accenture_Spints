package com.mindhub.order_service.dtos;

import com.mindhub.order_service.models.OrderStatus;

public record UpdateOrderRecord(OrderStatus orderStatus) {
}
