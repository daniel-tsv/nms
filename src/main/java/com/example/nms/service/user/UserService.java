package com.example.nms.service.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;

public interface UserService {

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> listUsers(int page, int size, String direction, String sortBy);

    User save(User user);

    User updateById(UUID id, User updatedUser);

    boolean delete(UUID id);

    boolean deleteByUsername(String username);

    User updateEntityFromDTO(User existingUser, UserDTO updatedUserDTO);

    User assignRole(UUID userId, UUID roleId);

    User removeRole(UUID userId, UUID roleId);

}
