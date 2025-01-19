package com.Sprint2.sprint2.repositories;

import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class RepositoriesUserTest {
    @Autowired
    UserRepository userRepository;

    UserEntity user;
    @BeforeEach
    public void setup(){
        user=new UserEntity("username","password","email", UserRoles.USER);
        userRepository.save(user);
    }

    @AfterEach
    public void delete_instances(){
        userRepository.deleteAll();
    }

    @Test
    public void ExistUserRepositoryTest(){
        UserEntity stored_user = userRepository.findByEmail("email").orElse(null);
        assertEquals(stored_user.getId(),user.getId());
        assertEquals(stored_user.getUsername(),user.getUsername());
        assertEquals(stored_user.getEmail(),user.getEmail());
    }

    @Test
    public void NotExistUserRepositoryTest(){
        UserEntity stored_user = userRepository.findByEmail("email2").orElse(null);
        assertEquals(stored_user,null);
    }

}
