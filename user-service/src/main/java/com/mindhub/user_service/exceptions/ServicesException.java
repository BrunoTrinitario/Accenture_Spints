package com.mindhub.user_service.exceptions;

import org.springframework.http.HttpStatus;

public class ServicesException extends RuntimeException {
    private HttpStatus httpStatus;
    public ServicesException(String message) {
        super(message);
    }

    public ServicesException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
