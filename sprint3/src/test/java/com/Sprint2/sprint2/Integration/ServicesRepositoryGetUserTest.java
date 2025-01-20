package com.Sprint2.sprint2.Integration;

import com.Sprint2.sprint2.dtos.UserRecord;
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

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryGetUserTest {

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
    public void GetUserByIdSuccessfulTest(){
        UserEntity expected_user = userRepository.findById(1L).orElse(null);
        try {
            UserEntity real_user = userService.getUserById(1L);
            assertEquals(expected_user.getId(),real_user.getId());
        } catch (UserException e) {
            fail();
        }
    }

    @Test
    public void GetUserByIdNotFoundTest(){
        try {
            userService.getUserById(10L);
            fail();
        } catch (UserException e) {
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void GetUserByEmailSuccessfulTest(){
        UserEntity expected_user = userRepository.findByEmail("email").orElse(null);
        try {
            Long real_user = userService.getIdByEmail("email");
            assertEquals(expected_user.getId(),real_user);
        } catch (UserException e) {
            fail();
        }
    }

    @Test
    public void GetUserByEmailNotFoundTest(){
        try {
            Long real_user = userService.getIdByEmail("email2");
            fail();
        } catch (UserException e) {
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void GetAllUsersSuccessfulTest(){
        Set<UserRecord> expected_set = new HashSet<>();
        expected_set.add(new UserRecord(user.getId(),user.getUsername(),user.getEmail()));
        Set<UserRecord> real_set = userService.getAllUsers();
        assertTrue(expected_set.equals(real_set));
    }
}
