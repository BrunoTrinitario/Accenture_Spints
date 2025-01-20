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
public class ServicesUpdateUserTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void UserNotFoundUpdateTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord("password","username");
        try{
            userService.updateUser("email",patchUserRecord);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.USR_NOT_EXIST, e.getMessage());
        }
    }

    @Test
    public void NullPasswordUpdateTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord(null,"username");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        try{
            userService.updateUser("email",patchUserRecord);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS, e.getMessage());
        }
    }

    @Test
    public void EmptyPasswordUpdateTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord("","username");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        try{
            userService.updateUser("email",patchUserRecord);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS, e.getMessage());
        }
    }

    @Test
    public void NullUsernameUpdateTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord("password",null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        try{
            userService.updateUser("email",patchUserRecord);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US, e.getMessage());
        }
    }

    @Test
    public void EmptyUsernameUpdateTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord("password","");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        try{
            userService.updateUser("email",patchUserRecord);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US, e.getMessage());
        }
    }

    @Test
    public void SuccessfulUpdateUserTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord("password","username");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserEntity()));
        try{
            userService.updateUser("email",patchUserRecord);
            assertTrue(true);
        } catch (UserException e) {
            fail();
        }
    }
}
