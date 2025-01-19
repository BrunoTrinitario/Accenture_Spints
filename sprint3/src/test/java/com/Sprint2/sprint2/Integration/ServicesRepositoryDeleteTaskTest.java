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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryDeleteTaskTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user,user2;
    private Task task,task2;

    @BeforeEach
    public void setup(){
        user=new UserEntity("username1","password1","email1", UserRoles.USER);
        user2=new UserEntity("username2","password2","email2", UserRoles.USER);
        task=new Task("title","description", TaskStatus.PENDING, user);
        task2=new Task("title2","description2", TaskStatus.PENDING, user2);
        userRepository.deleteAll();
        userRepository.save(user);
        userRepository.save(user2);
        taskRepository.deleteAll();
        taskRepository.save(task);
        taskRepository.save(task2);
    }

    @Test
    public void UserNotPermittedDeleteTaskTest() throws UserException {
        Iterator<Task> it = taskRepository.findAll().iterator();
        Task aux = it.next();

        try{
            taskService.deleteTask(user2.getEmail(), aux.getId());
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_TASK_NOT_PERMITTED,e.getMessage());
        }
    }

    @Test
    public void NullEmailDeleteTaskTest() throws UserException {
        try{
            taskService.deleteTask(null,1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailDeleteTaskTest() throws UserException {
        try{
            taskService.deleteTask("",1L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void NotFoundDeleteTaskTest() throws UserException {
        try{
            taskService.deleteTask(user.getEmail(), 3L);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.TASK_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void SuccessfulDeleteTaskTest() throws UserException {
        taskRepository.save(task);
        try{
            taskService.deleteTask(user.getEmail(), 3L);
            assertTrue(true);
        }catch (TaskException e){
            fail();
        }

    }
}
