package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.config.JwtUtils;
import com.Sprint2.sprint2.config.SecurityUtils;
import com.Sprint2.sprint2.dtos.NewTaskRecord;
import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerTaskTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private SecurityUtils securityUtils;

    @MockitoBean
    private TaskService taskService;

    private String token;

    private String email="email";

    private String uri = "/API/task";

    @BeforeEach
    public void setup() throws UserException {
        UserEntity user = new UserEntity("username", "password", email, UserRoles.USER);
        userRepository.save(user);
        token =  jwtUtils.generateToken(email);

    }

    @Test
    public  void testGetTaskById_Success() throws Exception {
        TaskRecord task = new TaskRecord(1L,"title","description", TaskStatus.PENDING);
        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        doReturn(task).when(taskService).getOneTaskById(anyString(),anyLong());
        mockMvc.perform(get(uri+"/1")
                        .header("Authorization", "Bearer " + token ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetAllTasks_Success() throws Exception{
        Set<TaskRecord> tasks = new HashSet<>();
        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        doReturn(tasks).when(taskService).getAllTasks();
        MvcResult result = mockMvc.perform(get(uri+"/all")
                        .header("Authorization", "Bearer " + token ))
                .andExpect(status().isOk()).andReturn();
        // Obtén el contenido de la respuesta como String
        String jsonResponse = result.getResponse().getContentAsString();

        // Deserializa el JSON a un objeto Java
        ObjectMapper objectMapper = new ObjectMapper();
        Set<TaskRecord> responseTasks = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(Set.class, TaskRecord.class)
        );

        assertTrue(tasks.equals(responseTasks));
    }

    @Test
    public void testPostOneTask_Success() throws Exception{
        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        Task task = new Task("title","description",TaskStatus.PENDING,new UserEntity());
        doReturn(task).when(taskService).createTask(anyString(),any(NewTaskRecord.class));
        NewTaskRecord newtask = new NewTaskRecord("title","decription",TaskStatus.PENDING);
        TaskRecord expected_Task = new TaskRecord(null,"title","description",TaskStatus.PENDING);
        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(post(uri)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newtask))) // Envío del cuerpo de la solicitud
                .andExpect(status().isCreated())
                .andReturn();


        TaskRecord responseTask = objectMapper.readValue(result.getResponse().getContentAsString(), TaskRecord.class);

        assertEquals(expected_Task,responseTask);
    }

    @Test
    public void testPutOneTask_Success() throws Exception{
        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        Task task = new Task("title","description",TaskStatus.PENDING,new UserEntity());
        doReturn(task).when(taskService).updateTask(anyString(),anyLong(),any(NewTaskRecord.class));
        NewTaskRecord newtask = new NewTaskRecord("title","decription",TaskStatus.PENDING);
        TaskRecord expected_Task = new TaskRecord(null,"title","description",TaskStatus.PENDING);
        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = mockMvc.perform(put(uri+"/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newtask))) // Envío del cuerpo de la solicitud
                .andExpect(status().isOk())
                .andReturn();

        TaskRecord responseTask = objectMapper.readValue(result.getResponse().getContentAsString(), TaskRecord.class);

        assertEquals(expected_Task,responseTask);
    }

    @Test
    public void testDeleteOneTask_Success() throws Exception{
        when(securityUtils.getAutenticatedEmail()).thenReturn(email);

        mockMvc.perform(delete(uri + "/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent())
                .andExpect(content().string(Constant.TASK_DELETE));
    }
}
