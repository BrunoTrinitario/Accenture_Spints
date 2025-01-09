package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.util.Constant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/API/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all tasks")
    })
    @GetMapping()
    public ResponseEntity<?> getAllTasks(){
        Set<TaskRecord> tasks = taskService.getAllTasks();
        return ResponseEntity.status(200).body(tasks);
    }
    @Operation(summary = "Get Task by ID", description = "Retrieve the details of a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @GetMapping("/{task_id}")
    public ResponseEntity<?> getOneTask(@Parameter(name = "task_id",description = "The task id to be found",required = false)@PathVariable Long task_id){
        return ResponseEntity.status(200).body(taskService.getOneTaskById(task_id));
    }

    @Operation(summary = "Get Tasks by User", description = "Retrieve all tasks for a specific user by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/all/{user_id}")
    public ResponseEntity<?> getTaskByUser(@Parameter(name = "user_id",description = "The user id to found all the tasks related",required = true)@PathVariable Long user_id){
        return ResponseEntity.status(200).body(taskService.getTaskByUserId(user_id));
    }
    @Operation(summary = "Create a Task", description = "Create a new task for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PostMapping("/{id_user}")
    public ResponseEntity<?> createTask(@Parameter(name = "user_id",description = "The user id of the tasks creator",required = true)@PathVariable Long id_user, @Parameter(name = "newTask",description = "The data of the task to be created",required = true)@RequestBody TaskRecord newTask){
        Task task=taskService.createTask(id_user, newTask);
        TaskRecord tr=new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(tr);
    }
    @Operation(summary = "Update a Task", description = "Update the details of an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @PutMapping("/{id_task}")
    public ResponseEntity<?> updateTask(@Parameter(name = "id_task",description = "The id of the task to be updated",required = true)@PathVariable Long id_task,@Parameter(name = "newTask",description = "The data of the task to be updated not including the id",required = true)@RequestBody TaskRecord newDataTask){
        Task task=taskService.updateTask(id_task,newDataTask);
        TaskRecord tr=new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(tr);
    }
    @Operation(summary = "Delete a Task", description = "Delete a task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @DeleteMapping("/{task_id}")
    public ResponseEntity<?> updateTask(@Parameter(name = "task_id",description = "The task id to be deleted",required = false)@PathVariable Long task_id){
        taskService.deleteTask(task_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Constant.TASK_DELETE);
    }
}
