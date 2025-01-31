package com.mindhub.user_service;

import com.mindhub.user_service.dtos.NewUserRecord;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.models.UserRole;
import com.mindhub.user_service.models.UserStatus;
import com.mindhub.user_service.repositories.UserRepository;
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
	public CommandLineRunner initData( PasswordEncoder passwordEncoder, UserRepository userRepository){
		return args -> {

			UserEntity user =  new UserEntity("admmin", passwordEncoder.encode("admin"),"admin@gmail.com",UserRole.ADMIN );
			user.setUserStatus(UserStatus.ACTIVE);
			userRepository.save(user);
			
			System.out.println("The users microservice its running");
		};

	}

}
