package com.notehub.notehub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.notehub.notehub.services.role.RoleService;
import com.notehub.notehub.services.user.UserService;

import entities.Role;
import entities.User;

@SpringBootApplication
public class NotehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotehubApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
		return args -> {

			if (roleService.findByAuthority("ADMIN").isPresent())
				return;

			Role userRole = new Role("USER");
			Role adminRole = new Role("ADMIN");

			User admin = new User("admin", passwordEncoder.encode("admin"), "admin@admin.com");
			User user = new User("user", passwordEncoder.encode("user"), "user@user.com");

			admin.getRoles().add(adminRole);
			user.getRoles().add(userRole);

			userService.createUser(admin);
			userService.createUser(user);
		};
	}
}
