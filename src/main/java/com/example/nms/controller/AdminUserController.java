package com.example.nms.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.exception.user.InvalidUserException;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;
import com.example.nms.validator.UserDTOValidator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserDTOValidator userDTOValidator;
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
                .orElseThrow(
                        () -> new UserIdNotFoundException(String.format(MessageConstants.USER_ID_NOT_FOUND, id)))));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Validated UserDTO userDTO, BindingResult br) {

        userDTOValidator.validate(userDTO, br);
        if (br.hasErrors())
            throw new InvalidUserException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        User createdUser = userService.save(userMapper.toEntity(userDTO));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getUuid()).toUri();

        return ResponseEntity.created(location).body(userMapper.toDTO(createdUser));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id,
            @RequestBody @Validated UserDTO userDTO, BindingResult br) {

        userDTOValidator.validate(userDTO, br);
        if (br.hasErrors())
            throw new InvalidUserException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        User updatedUser = userService.updateById(id, userMapper.toEntity(userDTO));

        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        if (userService.delete(id))
            return ResponseEntity.ok(MessageConstants.USER_DELETED);
        throw new UserIdNotFoundException(String.format(MessageConstants.USER_ID_NOT_FOUND, id));
    }

    // todo return AdminUserDTO
    @PostMapping("/{userId}/role/{roleId}")
    public ResponseEntity<User> assignRole(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId) {
        return ResponseEntity.ok(userService.assignRole(userId, roleId));
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    public ResponseEntity<User> removeRole(@PathVariable("userId") UUID userId, @PathVariable("roleId") UUID roleId) {
        return ResponseEntity.ok(userService.removeRole(userId, roleId));
    }
}
