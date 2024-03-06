package com.example.nms.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserDTO> getUserProfile() {

        User user = userService.getAuthenticatedUser();

        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PatchMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO updatedUserDTO, BindingResult br) {

        UUID userId = userService.getAuthenticatedUser().getUuid();
        User updatedUser = userService.updateUserFromUserDTO(userId, updatedUserDTO, br);

        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {

        userService.deleteById(userService.getAuthenticatedUser().getUuid());

        return ResponseEntity.noContent().build();
    }
}
