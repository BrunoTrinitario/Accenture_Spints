package com.mindhub.product_service.controllers;

import com.mindhub.product_service.exceptions.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> orderExceptionHandler(ProductException productException){
        return new ResponseEntity<>(productException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
