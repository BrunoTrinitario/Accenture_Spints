package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.dtos.NewUserRecord;
import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController
@RequestMapping("/API/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskController taskController;

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

    @Operation(summary = "User registration", description = "Register a new administrator")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NewUserRecord.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/user")
    public ResponseEntity<?> createAdmin(@Parameter(name = "newuser",description = "The data of the user to be created",required = true) NewUserRecord newuser) throws UserException {
        userService.createAdmin(newuser);
        return ResponseEntity.status(200).body(Constant.USR_CREATE);
    }

    @Operation(summary = "Delete User", description = "Deletes a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully.")
    })
    @DeleteMapping("/user/{id_user}")
    public ResponseEntity<?> deleteUser(@Parameter(name = "id_user",description = "The id of a user to be deleted",required = true)@PathVariable Long id_user) throws UserException {
        UserEntity user = userService.getUserById(id_user);
        userService.deleteUserByEmail(user.getEmail());
        return ResponseEntity.status(200).body(Constant.USR_DELETE);
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
