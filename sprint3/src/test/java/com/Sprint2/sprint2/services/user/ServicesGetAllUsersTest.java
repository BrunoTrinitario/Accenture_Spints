package com.Sprint2.sprint2.services.user;

import com.Sprint2.sprint2.dtos.UserRecord;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesGetAllUsersTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void SuccessfulGetAllUsersTest(){
        List<UserEntity> users_list = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(users_list);
        Set<UserRecord> set_users = userService.getAllUsers();
        Set<UserRecord> list_to_set_users = users_list.stream().map(userEntity -> new UserRecord(userEntity.getId(),userEntity.getUsername(),userEntity.getEmail())).collect(Collectors.toSet());
        assertTrue(set_users.equals(list_to_set_users));
    }
}
