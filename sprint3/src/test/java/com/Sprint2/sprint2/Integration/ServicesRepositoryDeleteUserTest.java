package com.Sprint2.sprint2.Integration;

import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.AfterEach;
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
public class ServicesRepositoryDeleteUserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity user;

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
    public void UserNotFoundDeleteTest(){
        try{
            userService.deleteUserByEmail("email2");
            fail();
        } catch (UserException e) {
            assertEquals(Constant.USR_NOT_EXIST, e.getMessage());
        }
    }

    @Test
    public void SuccessfulDeleteUserTest(){
        try{
            userService.deleteUserByEmail(user.getEmail());
            assertTrue(true);
        } catch (UserException e) {
            fail();
        }
    }

}
