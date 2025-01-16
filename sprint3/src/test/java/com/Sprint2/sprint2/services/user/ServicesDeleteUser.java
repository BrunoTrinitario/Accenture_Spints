package com.Sprint2.sprint2.services.user;

import com.Sprint2.sprint2.dtos.PatchUserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesDeleteUser {
    @Autowired
    UserService userService;

    @MockitoBean
    UserRepository userRepository;

    @Test
    public void UserNotFoundDeleteTest(){
        try{
            userService.deleteUserByEmail("email");
            fail();
        } catch (UserException e) {
            assertEquals(Constant.USR_NOT_EXIST, e.getMessage());
        }
    }

    @Test
    public void SuccessfulDeleteUserTest(){
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        try{
            userService.deleteUserByEmail("email");
            assertTrue(true);
        } catch (UserException e) {
            fail();
        }
    }
}
