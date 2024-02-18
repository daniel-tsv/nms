package com.example.nms.service.user;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.AdminUserDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.repository.UserRepository;
import com.example.nms.security.UserDetailsImpl;
import com.example.nms.service.role.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;

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
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
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
    public User updateUser(UserDTO updatedUserDTO) {
        return userRepository.save(
                userMapper.updateUserFromDTO(
                        getAuthenticatedUser(), updatedUserDTO));
    }

    @Override
    @Transactional
    public User updateUserFromAdminDTO(UUID existingUserId, AdminUserDTO updatedUserDTO) {

        User existingUser = userRepository.findById(existingUserId)
                .orElseThrow(() -> new UserIdNotFoundException(
                        String.format(MessageConstants.USER_ID_NOT_FOUND, existingUserId)));

        return userRepository.save(
                userMapper.updateUserFromAdminDTO(existingUser, updatedUserDTO));
    }

    @Override
    @Transactional
    public User assignRole(UUID userId, UUID roleId) {

        User user = this.findById(userId).orElseThrow(
                () -> new UserIdNotFoundException(
                        String.format(MessageConstants.USER_ID_NOT_FOUND, userId)));

        Role role = roleService.findById(roleId).orElseThrow(
                () -> new RoleIdNotFoundException(String.format(MessageConstants.ROLE_ID_NOT_FOUND, roleId)));

        user.getRoles().add(role);
        return userRepository.save(user);

    }

    @Override
    @Transactional
    public User removeRole(UUID userId, UUID roleId) {

        User user = this.findById(userId).orElseThrow(
                () -> new UserIdNotFoundException(
                        String.format(MessageConstants.USER_ID_NOT_FOUND, userId)));

        Role role = roleService.findById(roleId).orElseThrow(
                () -> new RoleIdNotFoundException(String.format(MessageConstants.ROLE_ID_NOT_FOUND, roleId)));

        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userDetails.getUser();
    }
}
