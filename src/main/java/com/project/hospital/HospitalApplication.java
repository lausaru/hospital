package com.project.hospital;

import com.project.hospital.security.models.Role;
import com.project.hospital.security.models.User;
import com.project.hospital.security.services.impl.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class HospitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_DOCTOR"));
			userService.saveRole(new Role(null, "ROLE_NURSE"));

			userService.saveUser(new User(null, "John Doe", "john", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Jane Carry", "jane", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Robert Jackson", "robert", "1234", new ArrayList<>()));

			userService.addRoleToUser("john", "ROLE_ADMIN");
			userService.addRoleToUser("jane", "ROLE_DOCTOR");
			userService.addRoleToUser("robert", "ROLE_NURSE");
		};
	}

}
