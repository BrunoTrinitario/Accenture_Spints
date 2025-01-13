package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.config.JwtUtils;
import com.Sprint2.sprint2.config.SecurityUtils;
import com.Sprint2.sprint2.dtos.LogInUserRecord;
import com.Sprint2.sprint2.dtos.NewUserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping("/login")
    public ResponseEntity<?> logInUser(@RequestBody LogInUserRecord user) throws UserException {
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

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Parameter(name = "newuser",description = "The data of the user to be created",required = true)@RequestBody NewUserRecord newuser) throws UserException {
        userService.createUser(newuser);
        return ResponseEntity.status(200).body(newuser);
    }

}
