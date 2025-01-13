package com.Sprint2.sprint2;

import com.Sprint2.sprint2.dtos.NewUserRecord;
import com.Sprint2.sprint2.model.Task;
import com.Sprint2.sprint2.model.TaskStatus;
import com.Sprint2.sprint2.model.UserEntity;
import com.Sprint2.sprint2.model.UserRoles;
import com.Sprint2.sprint2.repositories.TaskRepository;
import com.Sprint2.sprint2.repositories.UserRepository;
import com.Sprint2.sprint2.services.UserService;
import com.Sprint2.sprint2.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Sprint2Application {

	public static void main(String[] args) {
		SpringApplication.run(Sprint2Application.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserRepository userRepository, TaskRepository taskRepository, PasswordEncoder passwordEncoder){
		return args -> {


			UserEntity u1=new UserEntity("bruno",passwordEncoder.encode("bruno"),"asd", UserRoles.ADMIN);
			UserEntity u2=new UserEntity("bruno1","bruno1","asd1",UserRoles.USER);
			UserEntity u3=new UserEntity("bruno2","bruno2","asd2",UserRoles.ADMIN);

			Task t1= new Task("a","b", TaskStatus.IN_PROGRESS, u1);
			Task t11=new Task("A","B", TaskStatus.IN_PROGRESS, u1);
			Task t2= new Task("x","x", TaskStatus.IN_PROGRESS, u2);
			Task t3= new Task("y","y", TaskStatus.IN_PROGRESS, u3);

			userRepository.save(u1);
			userRepository.save(u2);
			userRepository.save(u3);
			taskRepository.save(t1);
			taskRepository.save(t11);
			taskRepository.save(t2);
			taskRepository.save(t3);


			System.out.println("The api its running");
		};
	}

}
