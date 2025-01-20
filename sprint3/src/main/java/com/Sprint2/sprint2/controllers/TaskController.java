package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.config.SecurityUtils;
import com.Sprint2.sprint2.dtos.NewTaskRecord;
import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.util.Constant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/API/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private SecurityUtils securityUtils;


    @Operation(summary = "Get Task by ID", description = "Retrieve the details of a task by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @GetMapping("/{task_id}")
    public ResponseEntity<?> getOneTask(@Parameter(name = "task_id",description = "The task id to be found",required = false)@PathVariable Long task_id) throws TaskException {
        String email= securityUtils.getAutenticatedEmail();
        return ResponseEntity.status(200).body(taskService.getOneTaskById(email,task_id));
    }

    @Operation(summary = "Get Tasks by User", description = "Retrieve all tasks for a specific user by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    @GetMapping("/all")
    public ResponseEntity<?> getTaskByUser() throws UserException {
        String email= securityUtils.getAutenticatedEmail();
        return ResponseEntity.status(200).body(taskService.getTaskByUserEmail(email));
    }

    @Operation(summary = "Create a Task", description = "Create a new task for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data.")
    })
    @PostMapping()
    public ResponseEntity<?> createTask(@Parameter(name = "newTask",description = "The data of the task to be created",required = true)@RequestBody NewTaskRecord newTask){
        String email= securityUtils.getAutenticatedEmail();
        TaskRecord tr=taskService.createTask(email, newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(tr);
    }

    @Operation(summary = "Update a Task", description = "Update the details of an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid input data."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @PutMapping("/{id_task}")
    public ResponseEntity<?> updateTask(@Parameter(name = "id_task",description = "The id of the task to be updated",required = true)@PathVariable Long id_task,@Parameter(name = "newTask",description = "The data of the task to be updated",required = true)@RequestBody NewTaskRecord newDataTask){
        String email= securityUtils.getAutenticatedEmail();
        Task task=taskService.updateTask(email,id_task,newDataTask);
        TaskRecord tr=new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(tr);
    }

    @Operation(summary = "Delete a Task", description = "Delete a task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Task not found.")
    })
    @DeleteMapping("/{task_id}")
    public ResponseEntity<?> deleteTask(@Parameter(name = "task_id",description = "The task id to be deleted",required = false)@PathVariable Long task_id){
        String email= securityUtils.getAutenticatedEmail();
        taskService.deleteTask(email,task_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Constant.TASK_DELETE);
    }
}
