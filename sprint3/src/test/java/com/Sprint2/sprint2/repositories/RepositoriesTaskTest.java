package com.Sprint2.sprint2.repositories;

import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class RepositoriesTaskTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private List<Task> tasks;

    @BeforeEach
    public void setup(){
        UserEntity user = new UserEntity("username","password","email", UserRoles.USER);
        userRepository.save(user);

        Task task1 = new Task("title","description", TaskStatus.PENDING, user);
        Task task2 = new Task("title2","description", TaskStatus.PENDING, user);

        taskRepository.save(task1);
        taskRepository.save(task2);

        tasks= new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
    }

    @AfterEach
    public void delete_instances(){
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void EmptyListTaskTest(){
        List<Task> list_task = taskRepository.findByUserId(2L);
        assertTrue(list_task.isEmpty());
    }


}
