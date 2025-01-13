package com.Sprint2.sprint2.exceptions;

import org.springframework.http.HttpStatus;

public class TaskException extends RuntimeException {
    private HttpStatus errorCode;
    public TaskException(String message,HttpStatus code) {

        super(message);
        errorCode=code;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
