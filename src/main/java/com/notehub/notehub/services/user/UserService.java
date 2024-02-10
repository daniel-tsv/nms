package com.notehub.notehub.services.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.entities.User;

public interface UserService {

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> listUsers(int page, int size, String direction, String sortBy);

    User save(User user);

    User updateById(UUID id, User updatedUser);

    User updateByUsername(String username, User updatedUser);

    void delete(UUID id);

    void deleteByUsername(String username);

    User updateEntityFromDTO(UUID uuid, UserDTO userDTO);
}
