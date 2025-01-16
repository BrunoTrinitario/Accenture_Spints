package com.Sprint2.sprint2.repositories;

import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RepositoriesUserTest {
    @Autowired
    UserRepository userRepository;

    UserEntity user;
    @BeforeEach
    public void setup(){
        user=new UserEntity("username","password","email", UserRoles.USER);
        userRepository.save(user);
    }

    @Test
    public void ExistUserRepositoryTest(){
        UserEntity stored_user = userRepository.findByEmail("email").orElse(null);
        assertEquals(stored_user,user);
    }

    @Test
    public void NotExistUserRepositoryTest(){
        UserEntity stored_user = userRepository.findByEmail("email2").orElse(null);
        assertEquals(stored_user,null);
    }

}
