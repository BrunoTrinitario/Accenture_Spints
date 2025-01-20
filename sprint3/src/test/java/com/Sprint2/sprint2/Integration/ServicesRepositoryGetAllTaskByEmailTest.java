package com.Sprint2.sprint2.Integration;

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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")

public class ServicesRepositoryGetAllTaskByEmailTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private Task task;

    @BeforeEach
    public void setup(){
        user=new UserEntity("username1","password1","email1", UserRoles.USER);
        task=new Task("title","description", TaskStatus.PENDING, user);

        user=userRepository.save(user);
        task=taskRepository.save(task);

    }

    @AfterEach
    public void delete_instances(){
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void SuccessfulGetByEmailTasksTest(){
        Set<TaskRecord> task_set = taskService.getTaskByUserEmail(user.getEmail());
        UserEntity us = userRepository.findByEmail(user.getEmail()).orElse(null);
        List<Task> aux_list = taskRepository.findByUserId(us.getId());
        Set<TaskRecord> expected_set = aux_list.stream().map(task -> new TaskRecord(task.getId(),task.getTitle(), task.getDescription(), task.getStatus())).collect(Collectors.toSet());;
        assertTrue(task_set.equals(expected_set));
    }

    @Test
    public void NullEmailGetByEmailTasksTest() throws UserException {
        try{
            taskService.getTaskByUserEmail(null);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailGetByEmailTasksTest() throws UserException {
        try{
            taskService.getTaskByUserEmail("");
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

}
