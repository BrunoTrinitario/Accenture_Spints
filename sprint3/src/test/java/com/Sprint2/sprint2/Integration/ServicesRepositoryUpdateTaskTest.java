package com.Sprint2.sprint2.Integration;

import com.Sprint2.sprint2.dtos.NewTaskRecord;
import com.Sprint2.sprint2.exceptions.TaskException;
import com.Sprint2.sprint2.exceptions.UserException;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.TaskRepository;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.TaskService;
import com.Sprint2.sprint2.util.Constant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
public class ServicesRepositoryUpdateTaskTest {
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user,user2;
    private Task task;
    private NewTaskRecord newTaskRecord;

    @BeforeEach
    public void setup(){
        user=new UserEntity("username1","password1","email1", UserRoles.USER);
        user2=new UserEntity("username2","password2","email2", UserRoles.USER);
        task=new Task("title","description", TaskStatus.PENDING, user);
        newTaskRecord = new NewTaskRecord("titulo","descripcion",TaskStatus.COMPLETED);

        userRepository.save(user);
        userRepository.save(user2);
        taskRepository.save(task);
    }

    @AfterEach
    public void delete_instances(){
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    public void NullEmailUpdateTaskTest() throws UserException {
        try{
            taskService.updateTask(null,1L,newTaskRecord);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void EmptyEmailUpdateTaskTest() throws UserException {
        try{
            taskService.updateTask("",1L,newTaskRecord);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void NotFoundUpdateTaskTest() throws UserException {
        try{
            taskService.updateTask(user.getEmail(),2L,newTaskRecord);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.TASK_NOT_EXIST,e.getMessage());
        }
    }

    @Test
    public void UserNotPermittedUpdateTaskTest() throws UserException {
        Iterator<Task> it = taskRepository.findAll().iterator();
        Task aux = it.next();
        try{
            taskService.updateTask(user2.getEmail(), aux.getId(),newTaskRecord);
            fail();
        }catch (TaskException e){
            assertEquals(Constant.USR_TASK_NOT_PERMITTED,e.getMessage());
        }
    }

    @Test
    public void SuccessfulUpdateTaskTest() throws UserException {
        Iterator<Task> it = taskRepository.findAll().iterator();
        Task aux = it.next();
        Task updated_task = taskService.updateTask(user.getEmail(), aux.getId(), newTaskRecord);
        assertEquals(newTaskRecord.title(),updated_task.getTitle());
        assertEquals(newTaskRecord.description(),updated_task.getDescription());
        assertEquals(newTaskRecord.status(),updated_task.getStatus());
    }
}
