package com.Sprint2.sprint2.services.user;

import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesGetIdByEmailTest {
    @Autowired
    UserService userService;

    @MockitoBean
    UserRepository userRepository;

    @Test
    public void UserNotFoundGetIdbyEmailTest(){
        try{
            userService.getIdByEmail("email");
            fail();
        } catch (UserException e) {
            assertEquals(Constant.USR_NOT_EXIST, e.getMessage());
        }
    }

    @Test
    public void SuccessfulGetUserByIdTest() throws UserException {
        UserEntity user = new UserEntity("username","password","email", UserRoles.USER);
        user.setId(1L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Long id = userService.getIdByEmail("email");

        assertEquals(1L,id);
    }


}
