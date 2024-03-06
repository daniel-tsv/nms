package com.example.nms.service.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;

import com.example.nms.dto.AdminUserDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;

public interface UserService {

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> listUsers(int page, int size, String direction, String sortBy);

    User createUser(User user);

    void deleteById(UUID id);

    void deleteByUsername(String username);

    User updateUserFromUserDTO(UUID existingUserId, UserDTO updatedUserDTO, Errors errors);

    User updateUserFromAdminDTO(UUID existingUserId, AdminUserDTO updatedAdminUserDTO);

    User assignRole(UUID userId, UUID roleId);

    User removeRole(UUID userId, UUID roleId);

    User getAuthenticatedUser();

}
