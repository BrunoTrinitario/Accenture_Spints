package com.Sprint2.sprint2.Integration;

import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryUserTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup(){
        UserEntity user = new UserEntity("username",  passwordEncoder.encode("password"), "email", UserRoles.USER);
        userRepository.save(user);
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
}
