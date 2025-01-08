package com.Sprint2.sprint2.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends Exception{
    private HttpStatus errorCode;
    public UserException(String message,HttpStatus code) {
        super(message);
        errorCode=code;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
