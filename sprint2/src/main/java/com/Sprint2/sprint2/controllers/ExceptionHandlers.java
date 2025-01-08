package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {

   @Operation(summary = "Handle UserException", description = "Handles exceptions specific to user-related errors")
   @ApiResponses(value = {
           @ApiResponse(responseCode = "400", description = "Bad Request: The request is invalid or malformed"),
           @ApiResponse(responseCode = "404", description = "Not Found: The requested user does not exist"),
           @ApiResponse(responseCode = "409", description = "Conflict: Conflict occurred with user-related operations")
    })
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> userExceptionHandler(UserException userException ){
        return new ResponseEntity<>(userException.getMessage(), userException.getErrorCode());
    }

    @Operation(summary = "Handle TaskException", description = "Handles exceptions specific to task-related errors.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request: The request is invalid or malformed."),
            @ApiResponse(responseCode = "404", description = "Not Found: The requested task does not exist.")
    })
    @ExceptionHandler(TaskException.class)
    public ResponseEntity<String> taskExceptionHandler(TaskException taskException){
        return new ResponseEntity<>(taskException.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
