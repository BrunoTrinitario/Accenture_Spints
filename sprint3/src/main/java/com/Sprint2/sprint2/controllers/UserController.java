package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.config.JwtUtils;
import com.Sprint2.sprint2.config.SecurityUtils;
import com.Sprint2.sprint2.dtos.PatchUserRecord;
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
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUtils securityUtils;

    @Operation(summary = "Update User", description = "Update the details of an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @PatchMapping()
    public ResponseEntity<?> patchUser(@Parameter(name = "newuser",description = "The new data to be updated",required = true)@RequestBody PatchUserRecord newdata) throws UserException {
        String email = securityUtils.getAutenticatedEmail();
        userService.updateUser(email, newdata);
        return ResponseEntity.status(200).body(Constant.USR_UPDATE);
    }

    @Operation(summary = "Delete User", description = "Deletes the logged user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully.")
    })
    @DeleteMapping()
    public ResponseEntity<?> deleteUser() throws UserException {
        String email = securityUtils.getAutenticatedEmail();
        userService.deleteUserByEmail(email);
        return ResponseEntity.status(200).body(Constant.USR_DELETE);
    }
}
