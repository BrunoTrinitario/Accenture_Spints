package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
@RestController
@RequestMapping("/API/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Operation(summary = "Get User by ID", description = "Retrieve the details of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/user/{id_user}")
    public ResponseEntity<?> getUser(@Parameter(name = "id_user",description = "The id of a user to be found",required = true)@PathVariable Long id_user) throws UserException {
        return ResponseEntity.status(200).body(userService.getUserRecord(id_user));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all users")
    })
    @GetMapping("/user/all")
    public ResponseEntity<?> getAllUsers(){
        Set<UserRecord> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all tasks")
    })
    @GetMapping("/task/all")
    public ResponseEntity<?> getAllTasks(){
        Set<TaskRecord> tasks = taskService.getAllTasks();
        return ResponseEntity.status(200).body(tasks);
    }
}
