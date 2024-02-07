package com.notehub.notehub.controllers;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.entities.User;
import com.notehub.notehub.exceptions.note.InvalidNoteException;
import com.notehub.notehub.exceptions.user.InvalidUserException;
import com.notehub.notehub.exceptions.user.UserNotFoundException;
import com.notehub.notehub.mappers.UserMapper;
import com.notehub.notehub.services.user.UserService;
import com.notehub.notehub.validators.UserValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserValidator userDTOValidator;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "sort", required = false, defaultValue = "uuid") String sortBy) {
        return ResponseEntity.ok(userMapper.toDTO(userService.listUsers(page, size, direction, sortBy).getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findByUUID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userMapper.toDTO(userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with this id does not exist"))));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Validated UserDTO userDTO, BindingResult br) {

        userDTOValidator.validate(userDTO, br);

        if (br.hasErrors())
            throw new InvalidUserException(
                    "user is not valid: " + br.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; ")));

        User createdUser = userService.createUser(userMapper.toEntity(userDTO));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getUuid()).toUri();

        return ResponseEntity.created(location).body(userMapper.toDTO(createdUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id,
            @RequestBody @Validated UserDTO userDTO, BindingResult br) {

        userDTOValidator.validate(userDTO, br);

        if (br.hasErrors())
            throw new InvalidNoteException(
                    "note is not valid: " + br.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; ")));

        User updatedUser = userService.updateUser(id, userMapper.toEntity(userDTO));

        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Successfully deleted user with id: {id}");
    }

}
