package com.notehub.notehub.controllers;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.entities.User;
import com.notehub.notehub.exceptions.user.InvalidUserException;
import com.notehub.notehub.mappers.UserMapper;
import com.notehub.notehub.services.AuthService;
import com.notehub.notehub.services.note.NoteService;
import com.notehub.notehub.services.user.UserService;
import com.notehub.notehub.validators.UserDTOValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final NoteService noteService;
    private final AuthService authService;
    private final UserDTOValidator userDTOValidator;

    @GetMapping
    public ResponseEntity<UserDTO> getUserProfile() {

        User user = authService.getAuthenticatedUser();

        UserDTO userDTO = userMapper.toDTO(user);
        userDTO.setNumberOfNotes(noteService.countUserNotes(user));

        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO updatedUserDTO, BindingResult br) {

        User userMakingRequest = authService.getAuthenticatedUser();

        userDTOValidator.validate(updatedUserDTO, br);
        if (br.hasErrors())
            throw new InvalidUserException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        User updatedUser = userService.updateEntityFromDTO(userMakingRequest.getUuid(), updatedUserDTO);

        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        throw new UnsupportedOperationException("Unimplemented method");
    }
}
