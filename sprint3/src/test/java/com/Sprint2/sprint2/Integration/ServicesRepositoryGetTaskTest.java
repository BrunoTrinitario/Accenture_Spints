package com.Sprint2.sprint2.Integration;

import com.Sprint2.sprint2.dtos.NewTaskRecord;
import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.TaskRepository;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryGetTaskTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user,user2;
    private Task task;
    private String mock_email = "email";

    @BeforeEach
    public void setup(){
        user=new UserEntity("username1","password1","email1", UserRoles.USER);
        user2=new UserEntity("username2","password2","email2", UserRoles.USER);
        task=new Task("title","description", TaskStatus.PENDING, user);

        user=userRepository.save(user);
        user2=userRepository.save(user2);
        task=taskRepository.save(task);

    }


    @AfterEach
    public void delete_instances(){
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }


    @Test
    public void UserNotFoundGetOneTaskTest() throws UserException {
        try{

            taskService.getOneTaskById(mock_email,1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void TaskNotFoundGetOneTaskTest() throws UserException {
        try{
            taskService.getOneTaskById(user.getEmail(), 4L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.TASK_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void UserNotPermittedGetOneTaskTest() throws UserException {
        try{
            taskService.getOneTaskById(user2.getEmail(), task.getId());
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_TASK_NOT_PERMITTED,e.getMessage());
        }
    }

    @Test
    public void NegativeIdGetOneTaskTest() throws UserException {
        try{
            taskService.getOneTaskById(user.getEmail(),-1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.TASK_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void NullEmailGetOneTaskTest() throws UserException {
        try{
            taskService.getOneTaskById(null,1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailGetOneTaskTest() throws UserException {
        try{
            taskService.getOneTaskById("",1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void LoadedTaskSet(){
        Set<TaskRecord> expected_set = taskRepository.findAll().stream().map(task-> new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus())).collect(Collectors.toSet());
        Set<TaskRecord> set_task=taskService.getAllTasks();
        assertTrue(expected_set.equals(set_task));
    }



}
