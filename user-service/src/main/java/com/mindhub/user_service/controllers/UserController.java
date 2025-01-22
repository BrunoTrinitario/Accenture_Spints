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

    @GetMapping("/{id}")
    public ResponseEntity<UserRecord> getUserById(@PathVariable Long id) throws UserException {
        UserRecord user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    @PostMapping
    public ResponseEntity<UserRecord> createUser(@RequestBody NewUserRecord newUserRecord) throws UserException {
        UserRecord user = userService.createUser(newUserRecord);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRecord> updateUser(@PathVariable Long id, @RequestBody UpdateUserRecord updateUserRecord) throws UserException {
        UserRecord updatedUser = userService.updateUser(id, updateUserRecord);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws UserException {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

}
