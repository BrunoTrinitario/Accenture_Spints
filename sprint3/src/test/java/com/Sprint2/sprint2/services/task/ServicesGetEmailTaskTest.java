package com.Sprint2.sprint2.services.task;

import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesGetEmailTaskTest {
    @Autowired
    TaskService taskService;
    @MockitoBean
    TaskRepository taskRepository;
    @MockitoBean
    UserService userService;

    @Test
    public void NullEmailGetByEmailTasksTest() throws UserException {
        when(userService.getIdByEmail(null)).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.getTaskByUserEmail(null);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailGetByEmailTasksTest() throws UserException {
        when(userService.getIdByEmail(anyString())).thenThrow(new UserException("string", HttpStatus.OK));
        try{
            taskService.getTaskByUserEmail("");
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void SuccessfulGetByEmailTasksTest(){
        List<Task> task_list = new ArrayList<>();
        when(taskRepository.findByUserId(anyLong())).thenReturn(task_list);
        Set<TaskRecord> task_set = taskService.getTaskByUserEmail("email");
        Set<TaskRecord> new_set_task =  task_list.stream().map(task-> new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus())).collect(Collectors.toSet());
        assertTrue(task_set.equals(new_set_task));
    }
}
