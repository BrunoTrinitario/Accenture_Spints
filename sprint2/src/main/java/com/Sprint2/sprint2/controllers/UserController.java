package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get User by ID", description = "Retrieve the details of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/{id_user}")
    public ResponseEntity<?> getUser(@Parameter(name = "id_user",description = "The id of a user to be found",required = true)@PathVariable Long id_user) throws UserException {
        return ResponseEntity.status(200).body(userService.getUserRecord(id_user));
    }

    @Operation(summary = "Create User", description = "Create a new user with the provided details and generating to it a random password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PostMapping()
    public ResponseEntity<?> createUser(@Parameter(name = "newuser",description = "The data of the user to be created",required = true)@RequestBody UserRecord newuser) throws UserException {
        userService.createUser(newuser);
        return ResponseEntity.status(200).body(Constant.USR_CREATE);
    }

    @Operation(summary = "Update User", description = "Update the details of an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @PatchMapping()
    public ResponseEntity<?> patchUser(@Parameter(name = "newuser",description = "The new data to be updated including the id",required = true)@RequestBody UserRecord newuser) throws UserException {
        userService.updateUser(newuser);
        return ResponseEntity.status(200).body(Constant.USR_UPDATE);
    }

    @Operation(summary = "Delete User", description = "Delete a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @DeleteMapping("/{id_user}")
    public ResponseEntity<?> deleteUser(@Parameter(name = "id_user",description = "The id of a user to be deleted",required = true)@PathVariable Long id_user) throws UserException {
        userService.deleteUserbyID(id_user);
        return ResponseEntity.status(200).body(Constant.USR_DELETE);
    }
}
