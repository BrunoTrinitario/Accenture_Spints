package com.mindhub.order_service.exceptions;

import java.util.function.Supplier;

public class OrderException extends Exception {
    public OrderException(String message) {
        super(message);
    }

}
