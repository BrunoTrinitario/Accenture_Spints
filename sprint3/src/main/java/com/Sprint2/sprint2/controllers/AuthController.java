package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.config.JwtUtils;
import com.Sprint2.sprint2.dtos.LogInUserRecord;
import com.Sprint2.sprint2.dtos.NewUserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Operation(summary = "User login", description = "Authenticate a user and return a JWT token")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", description = "JWT Token")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> logInUser(@Parameter(name = "user",description = "The data of the user to be logged in",required = true)@RequestBody LogInUserRecord user) throws UserException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.email(),
                        user.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(user.email());
        return ResponseEntity.status(200).body(token);
    }

    @Operation(summary = "User registration", description = "Register a new user")
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
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Parameter(name = "newuser",description = "The data of the user to be created",required = true)@RequestBody NewUserRecord newuser) throws UserException {
        userService.createUser(newuser);
        return ResponseEntity.status(200).body(newuser);
    }

}
