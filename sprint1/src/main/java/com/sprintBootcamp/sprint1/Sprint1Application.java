package com.sprintBootcamp.sprint1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Sprint1Application {

	public static void main(String[] args) {
		SpringApplication.run(Sprint1Application.class, args);
	}

	@Bean
	public CommandLineRunner initData(){
		return args -> {
			System.out.println("The api its running");
		};
	}
}
