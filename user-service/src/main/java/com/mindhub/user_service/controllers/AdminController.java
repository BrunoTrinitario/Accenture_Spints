package com.mindhub.user_service.controllers;

import com.mindhub.user_service.dtos.*;
import com.mindhub.user_service.exceptions.UserException;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.services.AdminService;
import com.mindhub.user_service.services.UserService;
import com.mindhub.user_service.util.Constants;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/API/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @PostMapping("/user")
    public ResponseEntity<UserRecord> createUser(@RequestBody NewUserRecord newUserRecord) throws UserException {
        UserRecord user = userService.createUser(newUserRecord);
        return new ResponseEntity<UserRecord>(user, HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<UserRecord> createAdmin(@RequestBody NewUserRecord newUserRecord) throws UserException {
        UserRecord admin = userService.createAdmin(newUserRecord);
        return new ResponseEntity<UserRecord>(admin, HttpStatus.OK);
    }

    @GetMapping("/users/all")
    public ResponseEntity<Set<UserRecord>> getAllUsers(){
        Set<UserRecord> userRecords = userService.getAllUsers();
        return new ResponseEntity<Set<UserRecord>>(userRecords, HttpStatus.OK);
    }

    @GetMapping("/orders/all")
    public ResponseEntity<Set<OrderDTO>> getAllOrders(){
        Set<OrderDTO> orderDTOS = adminService.getAllOrders();
        return new ResponseEntity<Set<OrderDTO>>(orderDTOS, HttpStatus.OK);
    }

    @GetMapping("/products/all")
    public ResponseEntity<Set<ExistentProductsRecord>> getAllProducts(){
        Set<ExistentProductsRecord> products = adminService.getAllProducts();
        return new ResponseEntity<Set<ExistentProductsRecord>>(products, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws UserException {
        userService.deleteUserById(id);
        return new ResponseEntity<String>(Constants.SUC_DEL_USER, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        adminService.deleteOrder(id);
        return new ResponseEntity<String>(Constants.SUC_DEL_ORDER, HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) throws UserException {
        adminService.deleteProduct(id);
        return new ResponseEntity<String>(Constants.SUC_DEL_PDT, HttpStatus.OK);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ExistentProductsRecord> updateProduct(@PathVariable Long id, @RequestBody NewProduct newProduct){
        ExistentProductsRecord existentProductsRecord = adminService.updateProduct(id,newProduct);
        return new ResponseEntity<ExistentProductsRecord>(existentProductsRecord, HttpStatus.OK);
    }
}
