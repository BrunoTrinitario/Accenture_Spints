package com.Sprint2.sprint2.services.task;


import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.TaskRepository;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesGetOneTaskTest {
    @Autowired
    TaskService taskService;
    @MockitoBean
    TaskRepository taskRepository;
    @MockitoBean
    UserService userService;

    UserEntity user1;
    Task task;

    @BeforeEach
    public void setup(){
        user1=new UserEntity("username1","password1","email1", UserRoles.USER);
        task=new Task("titulo","descripcion", TaskStatus.IN_PROGRESS, user1);
    }

    @Test
    public void UserNotFoundGetOneTaskTest() throws UserException {
        when(userService.getIdByEmail(anyString())).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            String mock_email = "email_prueba";
            taskService.getOneTaskById(mock_email,1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void TaskNotFoundGetOneTaskTest() throws UserException {
        when(userService.getIdByEmail(anyString())).thenReturn(1L);
        try{
            String mock_email = "email_prueba";
            taskService.getOneTaskById(mock_email,1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.TASK_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void UserNotPermittedGetOneTaskTest() throws UserException {
        when(userService.getIdByEmail(anyString())).thenReturn(2L);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        try{
            taskService.getOneTaskById("email2",1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_TASK_NOT_PERMITTED,e.getMessage());
        }
    }

    @Test
    public void NegativeIdGetOneTaskTest() throws UserException {
        try{
            taskService.getOneTaskById("email1",-1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.TASK_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void NullEmailGetOneTaskTest() throws UserException {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(userService.getIdByEmail(null)).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.getOneTaskById(null,1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailGetOneTaskTest() throws UserException {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(userService.getIdByEmail(anyString())).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.getOneTaskById("",1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }
}
