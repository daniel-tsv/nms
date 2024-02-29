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

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.AdminUserDTO;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "sort", required = false, defaultValue = "uuid") String sortBy) {

        return ResponseEntity.ok(
                userMapper.toAdminUserDTO(
                        userService.listUsers(page, size, direction, sortBy).getContent()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> findByUUID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(
                userMapper.toAdminUserDTO(
                        userService.findById(id)
                                .orElseThrow(() -> new UserIdNotFoundException(
                                        String.format(MessageConstants.USER_ID_NOT_FOUND, id)))));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdminUserDTO> updateUser(@PathVariable("id") UUID id,
            @RequestBody AdminUserDTO adminUserDTO) {
        return ResponseEntity.ok(
                userMapper.toAdminUserDTO(
                        userService.updateUserFromAdminDTO(id, adminUserDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
        if (userService.delete(id))
            return ResponseEntity.noContent().build();
        throw new UserIdNotFoundException(String.format(MessageConstants.USER_ID_NOT_FOUND, id));
    }

    @PostMapping("/{id}/roles/{roleId}")
    public ResponseEntity<AdminUserDTO> assignRole(@PathVariable("id") UUID userId,
            @PathVariable("roleId") UUID roleId) {
        return ResponseEntity.ok(
                userMapper.toAdminUserDTO(
                        userService.assignRole(userId, roleId)));
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    public ResponseEntity<AdminUserDTO> removeRole(@PathVariable("id") UUID userId,
            @PathVariable("roleId") UUID roleId) {
        return ResponseEntity.ok(
                userMapper.toAdminUserDTO(
                        userService.removeRole(userId, roleId)));
    }

}
