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

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryCreateTaskTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private Task task;
    private NewTaskRecord newTaskRecord;

    @BeforeEach
    public void setup(){
        user=new UserEntity("username1","password1","email1", UserRoles.USER);
        task=new Task("title","description", TaskStatus.PENDING, user);
        newTaskRecord = new NewTaskRecord("titulo","descripcion",TaskStatus.COMPLETED);
        user = userRepository.save(user);
        task = taskRepository.save(task);
    }

    @AfterEach
    public void delete_instances(){
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }



    @Test
    public void NullEmailCreateTaskTest() throws UserException {
        try{
            taskService.createTask(null,newTaskRecord);
        }catch(TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailCreateTaskTest() throws UserException {
        try{
            taskService.createTask("",newTaskRecord);
        }catch(TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void SuccessfulCreatedTaskTest() throws UserException {
        TaskRecord tr = taskService.createTask(user.getEmail(), newTaskRecord);
        assertEquals(newTaskRecord.title(),tr.title());
        assertEquals(newTaskRecord.description(),tr.description());
        assertEquals(newTaskRecord.status(),tr.status());
    }

}
