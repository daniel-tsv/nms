package com.notehub.notehub.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public Page<User> listUsers(int pageNumber, int pageSize, String direction, String sortBy) {
        Sort.Direction sortingDirection = "asc".equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

        return userRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortingDirection, sortBy)));
    }

    @Transactional
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(UUID id, User updatedUser) {
        updatedUser.setUuid(id);
        return userRepository.save(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
