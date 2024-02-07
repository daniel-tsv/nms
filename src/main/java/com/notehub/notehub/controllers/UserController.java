package com.notehub.notehub.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.mappers.UserMapper;
import com.notehub.notehub.services.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserDTO> getUserProfile(Principal principal) {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @PatchMapping
    public ResponseEntity<UserDTO> updateUser() {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        throw new UnsupportedOperationException("Unimplemented method");
    }

}
