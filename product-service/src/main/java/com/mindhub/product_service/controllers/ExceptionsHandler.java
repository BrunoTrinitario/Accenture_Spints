package com.mindhub.product_service.controllers;

import com.mindhub.product_service.exceptions.ProductException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    /**
     * Handles {@link ProductException} exceptions.
     * @param productException The exception to handle.
     * @return A {@link ResponseEntity} containing the error message and the appropriate HTTP status code.
     * If the exception has a custom HTTP status, it is used; otherwise, a BAD_REQUEST (400) status is returned.
     * @response 400 BAD_REQUEST - Default response for invalid product-related operations.
     * @response Custom - If the exception provides a specific HTTP status code.
     */
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<String> orderExceptionHandler(ProductException productException){
        if (productException.getHttpStatus()!=null)
            return new ResponseEntity<>(productException.getMessage(), productException.getHttpStatus());
        else
            return new ResponseEntity<>(productException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
