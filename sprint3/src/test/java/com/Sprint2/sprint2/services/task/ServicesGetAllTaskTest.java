package com.Sprint2.sprint2.services.task;

import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.repositories.TaskRepository;
import com.Sprint2.sprint2.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesGetAllTaskTest {
    @Autowired
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    private List<Task> tasks;

    @BeforeEach
    public void setup(){
        tasks= new ArrayList<>();
    }

    @Test
    public void EmptyTaskSet(){
        when(taskRepository.findAll()).thenReturn(tasks);
        Set<TaskRecord> set_task=taskService.getAllTasks();
        assertTrue(set_task.isEmpty());
    }

    @Test
    public void LoadedTaskSet(){
        Task t1 = new Task("titulo1","descripcion1", TaskStatus.IN_PROGRESS, new UserEntity());
        Task t2 = new Task("titulo2","descripcion2", TaskStatus.IN_PROGRESS, new UserEntity());
        tasks.add(t1);
        tasks.add(t2);
        Set<TaskRecord> expected_set = tasks.stream().map(task-> new TaskRecord(task.getId(),task.getTitle(),task.getDescription(),task.getStatus())).collect(Collectors.toSet());
        when(taskRepository.findAll()).thenReturn(tasks);
        Set<TaskRecord> set_task=taskService.getAllTasks();
        assertTrue(expected_set.equals(set_task));
    }
}
