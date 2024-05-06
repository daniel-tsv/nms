package com.example.nms.service.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.nms.dto.AdminUserDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;

public interface UserService {

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> listUsers(int page, int size, String direction, String sortBy);

    User create(User user);

    void deleteById(UUID id);

    void deleteByUsername(String username);

    User updateFromUserDTO(UUID existingUserId, UserDTO updatedUserDTO);

    User updateFromAdminDTO(UUID existingUserId, AdminUserDTO updatedAdminUserDTO);

    User assignRole(UUID userId, UUID roleId);

    User removeRole(UUID userId, UUID roleId);

    User getAuthenticatedUser();

    User createAdminUserIfNotExists(String adminUsername, String adminPassword, String adminEmail);
}
