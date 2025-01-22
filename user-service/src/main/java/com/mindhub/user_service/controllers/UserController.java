package com.mindhub.user_service.controllers;

import com.mindhub.user_service.dtos.NewUserRecord;
import com.mindhub.user_service.dtos.UpdateUserRecord;
import com.mindhub.user_service.dtos.UserRecord;
import com.mindhub.user_service.exceptions.UserException;
import com.mindhub.user_service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Retrieve a user by their ID.
     * @param id The ID of the user to retrieve.
     * @return A {@link ResponseEntity} containing the requested {@link UserRecord}.
     * @throws UserException If the user is not found or an error occurs.
     * @response 200 OK - The requested user.
     * @response 404 NOT FOUND - If the user is not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserRecord> getUserById(@PathVariable Long id) throws UserException {
        UserRecord user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Create a new user.
     * @param newUserRecord The {@link NewUserRecord} containing the user's details.
     * @return A {@link ResponseEntity} containing the created {@link UserRecord}.
     * @throws UserException If the user cannot be created.
     * @response 200 OK - The created user.
     * @response 400 BAD GATEWAY - if the input data its invalid
     * @response 409 BAD GATEWAY - if the email already exists
     */
    @PostMapping
    public ResponseEntity<UserRecord> createUser(@RequestBody NewUserRecord newUserRecord) throws UserException {
        UserRecord user = userService.createUser(newUserRecord);
        return ResponseEntity.ok(user);
    }

    /**
     * Update an existing user's details.
     * @param id The ID of the user to update.
     * @param updateUserRecord The {@link UpdateUserRecord} containing the updated details.
     * @return A {@link ResponseEntity} containing the updated {@link UserRecord}.
     * @throws UserException If the user cannot be updated or does not exist.
     * @response 200 OK - The updated user.
     * @response 400 BAD GATEWAY - if the input data its invalid
     * @response 404 NOT FOUND - If the user is not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserRecord> updateUser(@PathVariable Long id, @RequestBody UpdateUserRecord updateUserRecord) throws UserException {
        UserRecord updatedUser = userService.updateUser(id, updateUserRecord);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Delete a user by their ID.
     * @param id The ID of the user to delete.
     * @return A {@link ResponseEntity} with no content.
     * @throws UserException If the user cannot be deleted or does not exist.
     * @response 204 NO CONTENT - The user was successfully deleted.
     * @response 404 NOT FOUND - If the user is not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws UserException {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
