package com.Sprint2.sprint2.services.impl;

import com.Sprint2.sprint2.dtos.NewTaskRecord;
import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.repositories.TaskRepository;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserService userService;

    public TaskRecord getOneTaskById(String email,Long id) throws TaskException{
        try {
            Long id_user = userService.getIdByEmail(email);
            Task task = taskRepository.findById(id).orElseThrow(()-> new TaskException(Constant.TASK_NOT_EXIST, HttpStatus.NOT_FOUND));
            validateUserId(task.getUser().getId(), id_user);
            return new TaskRecord(task.getId(),task.getTitle(), task.getDescription(), task.getStatus());
        } catch (UserException e) {
            throw new TaskException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
    }

    public void validateUserId(Long user_task_id, Long user_id){
        if (user_task_id!=user_id){
            throw new TaskException(Constant.USR_TASK_NOT_PERMITTED,HttpStatus.UNAUTHORIZED);
        }
    }

    public Set<TaskRecord> getAllTasks(){
        List<Task> tasks = taskRepository.findAll();
        Set<TaskRecord> taskrecords = tasks.stream().map(task-> new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus())).collect(Collectors.toSet());
        return taskrecords;
    }

    public Set<TaskRecord> getTaskByUserEmail(String email) throws TaskException {
        try {
            Long id_user = userService.getIdByEmail(email);
            List<Task> tasks = taskRepository.findByUserId(id_user);
            Set<TaskRecord> taskrecords = tasks.stream().map(task-> new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus())).collect(Collectors.toSet());
            return taskrecords;
        } catch (UserException e) {
            throw new TaskException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

    }

    public Task createTask(String email, NewTaskRecord newTask) throws TaskException{
        validateTask(newTask.status());
        try {
            Long id_user = userService.getIdByEmail(email);
            UserEntity user = userService.getUserById(id_user);
            Task task = new Task(newTask.title(), newTask.description(), newTask.status(), user);
            return taskRepository.save(task);
        } catch (UserException e) {
            throw new TaskException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

    }

    public Task updateTask(String email, Long task_id,NewTaskRecord newDataTask) throws TaskException{
        try {
            Long id_user = userService.getIdByEmail(email);
            Task task=taskRepository.findById(task_id).orElseThrow(()->new TaskException(Constant.TASK_NOT_EXIST, HttpStatus.NOT_FOUND) );
            validateUserId(task.getUser().getId(),id_user);
            validateTask(newDataTask.status());
            task.setTitle(newDataTask.title());
            task.setDescription(newDataTask.description());
            task.setStatus(newDataTask.status());
            return taskRepository.save(task);
        } catch (UserException e) {
            throw new TaskException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

    }

    public void deleteTask(String email,Long id) throws TaskException {
        try {
            Long id_user = userService.getIdByEmail(email);
            Task task=taskRepository.findById(id).orElseThrow(()->new TaskException(Constant.TASK_NOT_EXIST, HttpStatus.NOT_FOUND) );
            validateUserId(task.getUser().getId(),id_user);
            taskRepository.deleteById(id);
        } catch (UserException e) {
            throw new TaskException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND);
        }

    }

    public void validateTask(TaskStatus status) throws TaskException{
        if (status==null){
            throw new TaskException(Constant.STATUS_NOT_VALID, HttpStatus.BAD_REQUEST);
        }
    }
}
