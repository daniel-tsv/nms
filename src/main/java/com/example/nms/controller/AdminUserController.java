package com.example.nms.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.nms.dto.AdminUserDTO;
import com.example.nms.entity.User;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "uuid") String sortBy) {

        List<User> users = userService.listUsers(page, size, direction, sortBy).getContent();

        return ResponseEntity.ok(userMapper.toAdminUserDTO(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> findByUUID(@PathVariable("id") UUID id) {

        User user = userService.findById(id).orElseThrow(() -> new UserIdNotFoundException(id));

        return ResponseEntity.ok(userMapper.toAdminUserDTO(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdminUserDTO> updateUser(@PathVariable("id") UUID id,
            @RequestBody AdminUserDTO adminUserDTO) {

        User updatedUser = userService.updateFromAdminDTO(id, adminUserDTO);

        return ResponseEntity.ok(userMapper.toAdminUserDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {

        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/roles/{roleId}")
    public ResponseEntity<AdminUserDTO> assignRole(@PathVariable("id") UUID userId,
            @PathVariable("roleId") UUID roleId) {

        User user = userService.assignRole(userId, roleId);

        return ResponseEntity.ok(userMapper.toAdminUserDTO(user));
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    public ResponseEntity<AdminUserDTO> removeRole(@PathVariable("id") UUID userId,
            @PathVariable("roleId") UUID roleId) {

        User user = userService.removeRole(userId, roleId);

        return ResponseEntity.ok(userMapper.toAdminUserDTO(user));
    }

}
