package com.example.nms.service.user;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.mapper.UserMapper;
import com.example.nms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
    public Page<User> listUsers(int page, int size, String direction, String sortBy) {

        page = Math.max(page, 0);
        size = Math.max(size, 1);
        sortBy = Arrays.asList("uuid", "username", "email", "createdAt").contains(sortBy) ? sortBy : "updatedAt";
        Sort.Direction sortDirection = Sort.Direction.ASC.name().equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return userRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateById(UUID id, User updatedUser) {
        updatedUser.setUuid(id);
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public boolean delete(UUID id) {

        if (!userRepository.existsById(id))
            return false;

        userRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteByUsername(String username) {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            userRepository.deleteByUsernameIgnoreCase(username);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public User updateByUsername(String username, User updatedUser) {
        updatedUser.setUsername(username);
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public User updateEntityFromDTO(User existingUser, UserDTO updatedUserDTO) {

        User updatedUser = userMapper.toEntity(updatedUserDTO);

        updatedUser.setUuid(existingUser.getUuid());
        updatedUser.setNotes(existingUser.getNotes());
        updatedUser.setPassword(existingUser.getPassword());
        updatedUser.setRoles(existingUser.getRoles());

        updatedUser.setAccountNonExpired(existingUser.isAccountNonExpired());
        updatedUser.setAccountNonLocked(existingUser.isAccountNonLocked());
        updatedUser.setCredentialsNonExpired(existingUser.isCredentialsNonExpired());
        updatedUser.setCreatedAt(existingUser.getCreatedAt());
        updatedUser.setEnabled(existingUser.isEnabled());

        return userRepository.save(updatedUser);
    }
}