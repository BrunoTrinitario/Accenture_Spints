package com.mindhub.order_service.controllers;

import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> orderExceptionHandler(OrderException orderException){
        return new ResponseEntity<>(orderException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderItemException.class)
    public ResponseEntity<String> orderItemExceptionHandler(OrderItemException orderItemException){
        return new ResponseEntity<>(orderItemException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
