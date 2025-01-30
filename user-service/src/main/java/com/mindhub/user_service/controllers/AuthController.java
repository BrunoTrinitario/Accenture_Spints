package com.mindhub.user_service.controllers;

import com.mindhub.user_service.dtos.NewUserRecord;
import com.mindhub.user_service.dtos.UserRecord;
import com.mindhub.user_service.dtos.LoginUserRecord;
import com.mindhub.user_service.exceptions.UserException;
import com.mindhub.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/API/auth")
public class AuthController {

    @Autowired
    UserService userService;

    /**
     * Create a new user.
     * @param newUserRecord The {@link NewUserRecord} containing the user's details.
     * @return A {@link ResponseEntity} containing the created {@link UserRecord}.
     * @throws UserException If the user cannot be created.
     * @response 200 OK - The created user.
     * @response 400 BAD GATEWAY - if the input data its invalid
     * @response 409 BAD GATEWAY - if the email already exists
     */
    @PostMapping("/register")
    public ResponseEntity<UserRecord> createUser(@RequestBody NewUserRecord newUserRecord) throws UserException {
        UserRecord user = userService.createUser(newUserRecord);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginUserRecord loginUserRecord) throws UserException {
        String token = userService.loginUser(loginUserRecord);
        return ResponseEntity.ok(token);
    }


}
