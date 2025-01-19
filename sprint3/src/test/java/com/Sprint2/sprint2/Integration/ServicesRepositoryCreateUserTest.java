package com.Sprint2.sprint2.Integration;

import com.Sprint2.sprint2.dtos.NewUserRecord;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryCreateUserTest {

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
        user = userRepository.save(user);
    }

    @AfterEach
    public void delete_instances(){
        userRepository.deleteAll();
    }

    @Test
    public void NullPasswordCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username2","email2",null);
        try{
            userService.createUser(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS,e.getMessage());
        }
    }

    @Test
    public void EmptyPasswordCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username2","email2","");
        try{
            userService.createUser(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS,e.getMessage());
        }
    }

    @Test
    public void NullUsernameCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord(null,"email2","password");
        try{
            userService.createUser(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US,e.getMessage());
        }
    }

    @Test
    public void EmptyUsernameCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("","email2","password");
        try{
            userService.createUser(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US,e.getMessage());
        }
    }

    @Test
    public void ExistentEmailCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username",user.getEmail(),"password");
        try{
            userService.createUser(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EXIST_MAIL,e.getMessage());
        }
    }

    @Test
    public void SuccessfulCreateUserTest(){
        NewUserRecord newuser= new NewUserRecord("username2","email2","password");
        try{
            userService.createUser(newuser);
            assertTrue(true);
        } catch (UserException e) {
            Assertions.fail();
        }
    }

    @Test
    public void NullPasswordCreateAdminTest(){
        NewUserRecord newuser= new NewUserRecord("username","email",null);
        try{
            userService.createAdmin(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS,e.getMessage());
        }
    }

    @Test
    public void EmptyPasswordCreateAdminTest(){
        NewUserRecord newuser= new NewUserRecord("username","email","");
        try{
            userService.createAdmin(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_PASS,e.getMessage());
        }
    }

    @Test
    public void NullUsernameCreateAdminTest(){
        NewUserRecord newuser= new NewUserRecord(null,"email","password");
        try{
            userService.createAdmin(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US,e.getMessage());
        }
    }

    @Test
    public void EmptyUsernameCreateAdminTest(){
        NewUserRecord newuser= new NewUserRecord("","email","password");
        try{
            userService.createAdmin(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EMPTY_US,e.getMessage());
        }
    }

    @Test
    public void ExistentEmailCreateAdminTest(){
        NewUserRecord newuser= new NewUserRecord("username", user.getEmail(), "password");
        try{
            userService.createAdmin(newuser);
            Assertions.fail();
        } catch (UserException e) {
            assertEquals(Constant.EXIST_MAIL,e.getMessage());
        }
    }

    @Test
    public void SuccessfulCreateAdminTest(){
        NewUserRecord newuser= new NewUserRecord("admin1","admin1","password");
        try{
            userService.createAdmin(newuser);
            assertTrue(true);
        } catch (UserException e) {
            Assertions.fail();
        }
    }
}
