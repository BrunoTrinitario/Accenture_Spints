package com.mindhub.order_service.controllers;

import com.mindhub.order_service.exceptions.OrderException;
import com.mindhub.order_service.exceptions.OrderItemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    /**
     * Handles {@link OrderException} exceptions.
     * @param orderException The exception to handle.
     * @return A {@link ResponseEntity} containing the error message and the appropriate HTTP status code.
     * If the exception has a custom HTTP status, it is used; otherwise, a BAD_REQUEST (400) status is returned.
     * @response 400 BAD_REQUEST - Default response for invalid order-related operations.
     * @response Custom - If the exception provides a specific HTTP status code.
     */
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<String> orderExceptionHandler(OrderException orderException){
        if (orderException.getHttpStatus()!=null)
            return new ResponseEntity<>(orderException.getMessage(), orderException.getHttpStatus());
        else
            return new ResponseEntity<>(orderException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link OrderItemException} exceptions.
     * @param orderItemException The exception to handle.
     * @return A {@link ResponseEntity} containing the error message and the appropriate HTTP status code.
     * If the exception has a custom HTTP status, it is used; otherwise, a BAD_REQUEST (400) status is returned.
     * @response 400 BAD_REQUEST - Default response for invalid order item-related operations.
     * @response Custom - If the exception provides a specific HTTP status code.
     */
    @ExceptionHandler(OrderItemException.class)
    public ResponseEntity<String> orderItemExceptionHandler(OrderItemException orderItemException){
        if (orderItemException.getHttpStatus()!=null)
            return new ResponseEntity<>(orderItemException.getMessage(), orderItemException.getHttpStatus());
        else
            return new ResponseEntity<>(orderItemException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
