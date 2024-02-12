package com.notehub.notehub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.notehub.notehub.entity.Role;
import com.notehub.notehub.entity.User;
import com.notehub.notehub.service.role.RoleService;
import com.notehub.notehub.service.user.UserService;

@SpringBootApplication
public class NotehubApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotehubApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
		return args -> {

			if (roleService.findByName("ROLE_ADMIN").isPresent())
				return;

			Role userRole = new Role("ROLE_USER");
			Role adminRole = new Role("ROLE_ADMIN");

			User admin = new User("admin", passwordEncoder.encode("admin"), "admin@admin.com");
			User user = new User("user", passwordEncoder.encode("user"), "user@user.com");

			admin.getRoles().add(adminRole);
			user.getRoles().add(userRole);

			userService.save(admin);
			userService.save(user);
		};
	}
}
