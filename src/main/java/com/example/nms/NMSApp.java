package com.example.nms;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.nms.constants.RoleConstants;
import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.service.role.RoleService;
import com.example.nms.service.user.UserService;

@SpringBootApplication
public class NMSApp {

    @Value("${nms.security.admin.username}")
    private String adminUsername;

    @Value("${nms.security.admin.password}")
    private String adminPassword;

    @Value("${nms.security.admin.email}")
    private String adminEmail;

    public static void main(String[] args) {
        SpringApplication.run(NMSApp.class, args);
    }

    @Bean
    CommandLineRunner run(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {

            if (roleService.findByName(RoleConstants.ROLE_USER).isEmpty())
                roleService.create(new Role(RoleConstants.ROLE_USER));

            if (roleService.findByName(RoleConstants.ROLE_ADMIN).isEmpty())
                roleService.create(new Role(RoleConstants.ROLE_USER));

            if (userService.findByUsername(adminUsername).isEmpty()) {
                User adminUser = new User(adminUsername, passwordEncoder.encode(adminPassword), adminEmail,
                        Collections.singleton(new Role(RoleConstants.ROLE_ADMIN)));
                userService.createUser(adminUser);
            }
        };
    }
}
