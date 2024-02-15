package com.example.nms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.service.role.RoleService;
import com.example.nms.service.user.UserService;

@SpringBootApplication
public class NMSApp {

	public static void main(String[] args) {
		SpringApplication.run(NMSApp.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
		return args -> {

			if (userService.findByUsername("admin").isPresent())
				return;

			Role userRole = new Role("ROLE_USER");
			roleService.create(userRole);

			Role adminRole = new Role("ROLE_ADMIN");
			User admin = new User("admin", passwordEncoder.encode("admin"), "admin@admin.com");
			admin.getRoles().add(adminRole);

			userService.save(admin);
		};
	}
}
