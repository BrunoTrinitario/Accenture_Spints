package com.Sprint2.sprint2.services.task;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesCreateTaskTest {
    @Autowired
    private TaskService taskService;
    @MockitoBean
    private TaskRepository taskRepository;
    @MockitoBean
    private UserService userService;

    NewTaskRecord taskRecord;
    @BeforeEach
    public void setup(){
        taskRecord=new NewTaskRecord("title","description", TaskStatus.PENDING);
    }

    @Test
    public void NullEmailCreateTaskTest() throws UserException {
        when(userService.getIdByEmail(null)).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.createTask(null,taskRecord);
        }catch(TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailCreateTaskTest() throws UserException {
        when(userService.getIdByEmail(anyString())).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.createTask("",taskRecord);
        }catch(TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void SuccessfulCreatedTaskTest() throws UserException {
        Task taskEntity = new Task("title","description",TaskStatus.PENDING,new UserEntity());
        when(taskRepository.save(any(Task.class))).thenReturn(taskEntity);
        TaskRecord task = taskService.createTask("email",taskRecord);
        assertEquals(task.title(),taskEntity.getTitle());
        assertEquals(task.description(),taskEntity.getDescription());
        assertEquals(task.status(),taskEntity.getStatus());
    }


}
