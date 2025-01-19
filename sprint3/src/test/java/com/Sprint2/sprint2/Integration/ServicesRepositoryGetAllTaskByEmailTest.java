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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private UserEntity user,user2;
    private Task task,task2,task3;

    @BeforeEach
    public void setup(){
        user=new UserEntity("username1","password1","email1", UserRoles.USER);
        user2=new UserEntity("username2","password2","email2", UserRoles.USER);
        task=new Task("title","description", TaskStatus.PENDING, user);
        task2=new Task("title2","description2", TaskStatus.PENDING, user2);
        task3=new Task("title","description", TaskStatus.PENDING, user);

        userRepository.deleteAll();
        userRepository.save(user);
        userRepository.save(user2);
        taskRepository.deleteAll();
        taskRepository.save(task);
        taskRepository.save(task2);
        taskRepository.save(task3);

    }

    @Test
    public void SuccessfulGetByEmailTasksTest(){
        Set<TaskRecord> task_set = taskService.getTaskByUserEmail(user.getEmail());
        Task taskAux = taskRepository.findById(1L).orElse(null);
        TaskRecord tr1 = new TaskRecord(taskAux.getId(),taskAux.getTitle(),taskAux.getDescription(),taskAux.getStatus());
        taskAux = taskRepository.findById(3L).orElse(null);
        TaskRecord tr2 = new TaskRecord(taskAux.getId(),taskAux.getTitle(),taskAux.getDescription(),taskAux.getStatus());
        Set<TaskRecord> new_set_task = new HashSet<>();
        new_set_task.add(tr1);
        new_set_task.add(tr2);
        assertTrue(task_set.equals(new_set_task));
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
