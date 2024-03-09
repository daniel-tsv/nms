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
import com.example.nms.exception.user.UserValidationException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;
import com.example.nms.validator.UserDTOValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserDTOValidator userDTOValidator;

    @GetMapping
    public ResponseEntity<UserDTO> getUserProfile() {

        User user = userService.getAuthenticatedUser();

        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PatchMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO updatedUserDTO, BindingResult br) {

        UUID userId = userService.getAuthenticatedUser().getUuid();

        // set updatedUserDTO UUID, for validator to work properly
        updatedUserDTO.setUuid(userId);
        userDTOValidator.validate(updatedUserDTO, br);
        if (br.hasErrors())
            throw new UserValidationException(br);

        User updatedUser = userService.updateFromUserDTO(userId, updatedUserDTO);

        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {

        userService.deleteById(userService.getAuthenticatedUser().getUuid());

        return ResponseEntity.noContent().build();
    }
}
