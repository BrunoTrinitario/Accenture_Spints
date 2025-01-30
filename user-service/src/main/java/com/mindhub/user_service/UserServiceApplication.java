package com.mindhub.user_service;

import com.mindhub.user_service.dtos.NewUserRecord;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.models.UserRole;
import com.mindhub.user_service.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UserService userService, PasswordEncoder passwordEncoder){
		return args -> {

			//NewUserRecord user = new NewUserRecord("admin", "admin", "email@hotmail.com");
			//NewUserRecord user2 = new NewUserRecord("user", "user", "user@hotmail.com");
			//userService.createAdmin(user);
			//userService.createUser(user2);

			System.out.println("The users microservice its running");
		};

	}

}
