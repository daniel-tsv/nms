package com.notehub.notehub.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

public interface UserService {

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> listUsers(int page, int size, String direction, String sortBy);

    User createUser(User user);

    User updateUser(UUID id, User updatedUser);

    void deleteUser(UUID id);
}
