package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.config.JwtUtils;
import com.Sprint2.sprint2.config.SecurityUtils;
import com.Sprint2.sprint2.dtos.TaskRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
}
