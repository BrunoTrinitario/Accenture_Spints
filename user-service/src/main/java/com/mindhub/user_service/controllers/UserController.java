package com.mindhub.user_service.controllers;

import com.mindhub.user_service.config.JwtUtils;
import com.mindhub.user_service.dtos.NewUserRecord;
import com.mindhub.user_service.dtos.UpdateUserRecord;
import com.mindhub.user_service.dtos.UserRecord;
import com.mindhub.user_service.dtos.UserRegistrationRecord;
import com.mindhub.user_service.exceptions.UserException;
import com.mindhub.user_service.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.message.TokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/API/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Retrieve a user by their ID.
     * @return A {@link ResponseEntity} containing the requested {@link UserRecord}.
     * @throws UserException If the user is not found or an error occurs.
     * @response 200 OK - The requested user.
     * @response 404 NOT FOUND - If the user is not found.
     */
    @GetMapping()
    public ResponseEntity<UserRecord> getUserById(HttpServletRequest request) throws UserException {
        String email = jwtUtils.getEmailFromToken(request.getHeader("Authorization"));
        Long id = userService.getIdByEmail(email);
        UserRecord user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Retrieve a user by their ID.
     * @param email The ID of the user to retrieve.
     * @return A {@link ResponseEntity} containing the requested {@link UserRecord}.
     * @throws UserException If the user is not found or an error occurs.
     * @response 200 OK - The requested user.
     * @response 404 NOT FOUND - If the user is not found.
     */
    @GetMapping("/private/email/{email}")
    public ResponseEntity<Long> getIdByEmail(@PathVariable String email) throws UserException {
        UserRegistrationRecord user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user.id());
    }

    /**
     * Update an existing user's details.
     * @param updateUserRecord The {@link UpdateUserRecord} containing the updated details.
     * @return A {@link ResponseEntity} containing the updated {@link UserRecord}.
     * @throws UserException If the user cannot be updated or does not exist.
     * @response 200 OK - The updated user.
     * @response 400 BAD GATEWAY - if the input data its invalid
     * @response 404 NOT   FOUND - If the user is not found.
     */
    @PutMapping()
    public ResponseEntity<UserRecord> updateUser(@RequestBody UpdateUserRecord updateUserRecord,HttpServletRequest request) throws UserException {
        String email = jwtUtils.getEmailFromToken(request.getHeader("Authorization"));
        Long id = userService.getIdByEmail(email);
        UserRecord updatedUser = userService.updateUser(id, updateUserRecord);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete a user by their ID.
     * @return A {@link ResponseEntity} with no content.
     * @throws UserException If the user cannot be deleted or does not exist.
     * @response 204 NO CONTENT - The user was successfully deleted.
     * @response 404 NOT FOUND - If the user is not found.
     */
    @DeleteMapping()
    public ResponseEntity<Void> deleteUserById(HttpServletRequest request) throws UserException {
        String email = jwtUtils.getEmailFromToken(request.getHeader("Authorization"));
        Long id = userService.getIdByEmail(email);
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }


}
