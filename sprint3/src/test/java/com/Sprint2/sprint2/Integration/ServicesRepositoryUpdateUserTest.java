package com.Sprint2.sprint2.Integration;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryUpdateUserTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    UserEntity user;

    @BeforeEach
    public void setup(){
        user = new UserEntity("username",  passwordEncoder.encode("password"), "email", UserRoles.USER);
        userRepository.deleteAll();
        userRepository.save(user);
    }

    @Test
    public void UserNotFoundUpdateTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord("password","username");
        try{
            userService.updateUser("email1",patchUserRecord);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.USR_NOT_EXIST, e.getMessage());
        }
    }

    @Test
    public void NullPasswordUpdateTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord(null,"username");
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
        try{
            userService.updateUser("email",patchUserRecord);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US, e.getMessage());
        }
    }

    @Test
    public void SuccessfulUpdateUserTest(){
        PatchUserRecord patchUserRecord = new PatchUserRecord("password","new_username");
        try{
            userService.updateUser("email",patchUserRecord);
            Long id = userService.getIdByEmail("email");
            UserEntity user = userService.getUserById(id);
            assertEquals(user.getUsername(),"new_username");
            assertTrue(passwordEncoder.matches("password", user.getPassword()));
        } catch (UserException e) {
            fail();
        }
    }
}
