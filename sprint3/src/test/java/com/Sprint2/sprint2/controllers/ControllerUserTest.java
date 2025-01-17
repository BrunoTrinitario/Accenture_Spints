package com.Sprint2.sprint2.controllers;

import com.Sprint2.sprint2.config.JwtUtils;
import com.Sprint2.sprint2.config.SecurityUtils;
import com.Sprint2.sprint2.dtos.PatchUserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private SecurityUtils securityUtils;

    private String token;

    private String email="email";

    private String uri = "/API/user";

    @BeforeEach
    public void setup() throws UserException {
        UserEntity user = new UserEntity("username", "password", email, UserRoles.USER);
        userRepository.save(user);
        token =  jwtUtils.generateToken(email);
    }

    @Test
    void testPatchUser_Success() throws Exception {
        String body_to_send = "{\"password\":\"password\",\"username\":\"username\"}";

        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        doNothing().when(userService).updateUser(anyString(),any(PatchUserRecord.class));

        mockMvc.perform(patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body_to_send)
                        .header("Authorization", "Bearer " + token ))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.USR_UPDATE));
    }

    @Test
    void testPatchUser_InvalidData() throws Exception {
        String body_to_send = "";
        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        doNothing().when(userService).updateUser(anyString(),any(PatchUserRecord.class));
        mockMvc.perform(patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body_to_send)
                        .header("Authorization", "Bearer " + token ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPatchUser_UserNotFound() throws Exception {
        String body_to_send = "";
        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        doThrow(new UserException(Constant.USR_NOT_EXIST, HttpStatus.NOT_FOUND)).when(userService).updateUser(anyString(),any(PatchUserRecord.class));
        mockMvc.perform(patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body_to_send)
                        .header("Authorization", "Bearer " + token ))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        String body_to_send = "{\"password\":\"password\",\"username\":\"username\"}";

        when(securityUtils.getAutenticatedEmail()).thenReturn(email);
        doNothing().when(userService).deleteUserByEmail(anyString());

        mockMvc.perform(delete(uri)
                        .header("Authorization", "Bearer " + token ))
                .andExpect(status().isOk())
                .andExpect(content().string(Constant.USR_DELETE));
    }


}
