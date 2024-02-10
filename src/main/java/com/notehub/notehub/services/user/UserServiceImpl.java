package com.notehub.notehub.services.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.entities.User;
import com.notehub.notehub.exceptions.user.UserNotFoundException;
import com.notehub.notehub.mappers.UserMapper;
import com.notehub.notehub.repositories.UserRepository;

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
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
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
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        userRepository.deleteByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional
    public User updateByUsername(String username, User updatedUser) {
        updatedUser.setUsername(username);
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public User updateEntityFromDTO(UUID uuid, UserDTO userDTO) {

        User existingUser = findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("Unable to update - user id was not found"));

        User user = userMapper.toEntity(userDTO);

        user.setUuid(existingUser.getUuid());
        user.setNotes(existingUser.getNotes());
        user.setPassword(existingUser.getPassword());
        user.setRoles(existingUser.getRoles());

        user.setAccountNonExpired(existingUser.isAccountNonExpired());
        user.setAccountNonLocked(existingUser.isAccountNonLocked());
        user.setCredentialsNonExpired(existingUser.isCredentialsNonExpired());
        user.setCreatedAt(existingUser.getCreatedAt());
        user.setEnabled(existingUser.isEnabled());

        return userRepository.save(user);
    }
}
