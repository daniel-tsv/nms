package com.example.nms.service.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.exception.user.UserIdNotFoundException;
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
    public User updateEntityFromDTO(UUID uuid, UserDTO updatedUserDTO) {

        User existingUser = findById(uuid)
                .orElseThrow(
                        () -> new UserIdNotFoundException("Unable to update user - id '" + uuid + "' was not found"));
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
