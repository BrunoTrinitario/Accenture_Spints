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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesDeleteTaskTest {
    @Autowired
    TaskService taskService;
    @MockitoBean
    TaskRepository taskRepository;
    @MockitoBean
    UserService userService;

    @Test
    public void NullEmailDeleteTaskTest() throws UserException {
        when(userService.getIdByEmail(null)).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.deleteTask(null,1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailDeleteTaskTest() throws UserException {
        when(userService.getIdByEmail(anyString())).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.deleteTask("",1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void NotFoundDeleteTaskTest() throws UserException {
        when(taskRepository.findById(anyLong())).thenThrow(new TaskException(Constant.TASK_NOT_EXIST, HttpStatus.NOT_FOUND));
        try{
            taskService.deleteTask("",1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.TASK_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void UserNotPermittedDeleteTaskTest() throws UserException {
        UserEntity user = new UserEntity("username","password","email", UserRoles.USER);
        user.setId(1L);
        Task task = new Task("title","description", TaskStatus.PENDING, user);
        when(userService.getIdByEmail(anyString())).thenReturn(1l);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        try{
            taskService.deleteTask("",1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_TASK_NOT_PERMITTED,e.getMessage());
        }
    }

    @Test
    public void SuccessfulDeleteTaskTest() throws UserException {
        UserEntity user = new UserEntity("username","password","email", UserRoles.USER);
        user.setId(1L);
        Task task = new Task("title","description", TaskStatus.PENDING, user);

        when(userService.getIdByEmail(anyString())).thenReturn(1L);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        try{
            taskService.deleteTask("",1L);
            assertTrue(true);
        }catch (TaskException e){
            fail();
        }

    }
}
