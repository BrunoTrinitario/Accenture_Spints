package com.mindhub.user_service.controllers;

import com.mindhub.user_service.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    /**
     * Handles {@link UserException} exceptions.
     * @param userException The exception to handle.
     * @return A {@link ResponseEntity} containing the error message and the appropriate HTTP status code.
     * If the exception has a custom HTTP status, it is used; otherwise, a BAD_REQUEST (400) status is returned.
     * @response 400 BAD_REQUEST - Default response for invalid user-related operations.
     * @response Custom - If the exception provides a specific HTTP status code.
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> orderExceptionHandler(UserException userException){
        if (userException.getHttpStatus()!=null)
            return new ResponseEntity<>(userException.getMessage(), userException.getHttpStatus());
        else
            return new ResponseEntity<>(userException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
