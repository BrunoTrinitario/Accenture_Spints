package com.Sprint2.sprint2.services.user;

import com.Sprint2.sprint2.dtos.NewUserRecord;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesCreateUserTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void NullPasswordCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username","email",null);
        try{
            userService.createUser(newuser);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS,e.getMessage());
        }
    }

    @Test
    public void EmptyPasswordCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username","email","");
        try{
            userService.createUser(newuser);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS,e.getMessage());
        }
    }

    @Test
    public void NullUsernameCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord(null,"email","password");
        try{
            userService.createUser(newuser);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US,e.getMessage());
        }
    }

    @Test
    public void EmptyUsernameCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("","email","password");
        try{
            userService.createUser(newuser);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US,e.getMessage());
        }
    }

    @Test
    public void ExistentEmailCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username","email","password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        try{
            userService.createUser(newuser);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EXIST_MAIL,e.getMessage());
        }
    }

    @Test
    public void SuccessfulCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username","email","password");
        try{
            userService.createUser(newuser);
            assertTrue(true);
        } catch (UserException e) {
            fail();
        }
    }


}
