package com.sprintBootcamp.sprint1.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/list")
public class ApiController {

    @GetMapping()
    @Operation(summary = "Handles the retrieval of all lists.", description = "This method provide an array of every list of determinant user")
    @ApiResponse(responseCode = "200", description = "Data successfully sent")
    public ResponseEntity<?> getAllLists(){
        return ResponseEntity.status(200).body("The GET petition has been resolved correctly");
    }

    @GetMapping("/{id_list}")
    @Operation(summary = "Handles the retrieval of a specific list by its ID", description = "This method provide a list by its own ID")
    @ApiResponse(responseCode = "200", description = "Data successfully sent")
    public ResponseEntity<?> getListById(@Parameter(name= "id_list", required = false, description = "The id of any list") @PathVariable String id_list ){
        return ResponseEntity.status(200).body("The GET petition has been resolved correctly "+ "the list is: " + id_list);
    }

    @PostMapping()
    @Operation(summary = "Handles the creation of a new list", description = "This method creates a new list")
    @ApiResponse(responseCode = "201", description = "List successfully created")
    public ResponseEntity<?> createList(@Parameter(name= "data", required = true, description = "a list object in JSON format")@RequestBody String data){
        return ResponseEntity.status(201).body("The POST petition has been resolved correctly: " + data);
    }

    @PatchMapping("/{id_list}")
    @Operation(summary = "Handles the modification of an existing list by its ID", description = "This method modifies an existent list")
    @ApiResponse(responseCode = "202", description = "List successfully modified")
    public ResponseEntity<?> modifyList(@Parameter(name= "data", required = true, description = "a list object in JSON format")@RequestBody String data, @Parameter(name= "id_list", required = false, description = "The id of any list")@PathVariable String id_list){
        return ResponseEntity.status(202).body("The PATCH petition has been resolved correctly "+ "the list is: " + id_list + " new data into the list is: " + data);
    }

    @DeleteMapping("/{id_list}")
    @Operation(summary = "Handles the deletion of a specific list by its ID", description = "This method delete an existent list")
    @ApiResponse(responseCode = "202", description = "List successfully deleted")
    public ResponseEntity<?> deleteList(@Parameter(name= "id_list", required = false, description = "The id of any list")@PathVariable String id_list){
        return ResponseEntity.status(202).body("The DELETE petition has been resolved correctly "+ "the list is: " + id_list);
    }

    @PutMapping()
    @Operation(summary = "Handles the creation of a new list using a PUT request", description = "This method creates a new list")
    public ResponseEntity<?> putNewList(@RequestBody String data){
        return this.createList(data);
    }

    @PutMapping("/{id_list}")
    @Operation(summary = "Handles the replacement or update of an existing list by its ID using a PUT request", description = "This method modifies an existent list")
    public ResponseEntity<?> putExistentList(@Parameter(name= "data", required = true, description = "a list object in JSON format")@RequestBody String data, @Parameter(name= "id_list", required = false, description = "The id of any list")@PathVariable String id_list){
        return this.modifyList(data,id_list);
    }
}
