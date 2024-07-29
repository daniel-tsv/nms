package com.example.nms.service.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.constants.RoleConstants;
import com.example.nms.dto.AdminUserDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.exception.user.UserNameNotFoundException;
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

        PageRequest request = PageRequest.of(page, size, sort);

        return userRepository.findAll(request);
    }

    @Override
    @Transactional
    public User create(User user) {

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {

        if (!userRepository.existsById(id))
            throw new UserIdNotFoundException(id);

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {

        if (!userRepository.existsByUsernameIgnoreCase(username))
            throw new UserNameNotFoundException(username);

        userRepository.deleteByUsernameIgnoreCase(username);
    }

    @Override
    @Transactional
    public User updateFromUserDTO(UUID existingUserId, UserDTO updatedUserDTO) {

        User existingUser = userRepository.findById(existingUserId)
                .orElseThrow(() -> new UserIdNotFoundException(existingUserId));

        User updatedUser = userMapper.updateUserFromDTO(existingUser, updatedUserDTO);

        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public User updateFromAdminDTO(UUID existingUserId, AdminUserDTO updatedUserDTO) {

        User existingUser = userRepository.findById(existingUserId)
                .orElseThrow(() -> new UserIdNotFoundException(existingUserId));

        User updatedUser = userMapper.updateUserFromAdminDTO(existingUser, updatedUserDTO);

        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public User assignRole(UUID userId, UUID roleId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserIdNotFoundException(userId));
        Role role = roleService.findById(roleId).orElseThrow(
                () -> new RoleIdNotFoundException(roleId));

        user.getRoles().add(role);

        return userRepository.save(user);

    }

    @Override
    @Transactional
    public User removeRole(UUID userId, UUID roleId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserIdNotFoundException(userId));
        Role role = roleService.findById(roleId).orElseThrow(
                () -> new RoleIdNotFoundException(roleId));

        user.getRoles().remove(role);

        return userRepository.save(user);
    }

    @Override
    public User getAuthenticatedUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return userDetails.getUser();
    }

    @Override
    @Transactional
    public User createAdminUserIfNotExists(String adminUsername, String adminPassword, String adminEmail) {

        Optional<Role> adminRoleOptional = roleService.findByName(RoleConstants.ROLE_ADMIN);
        Role adminRole = adminRoleOptional.orElseGet(() -> roleService.create(new Role(RoleConstants.ROLE_ADMIN)));

        Optional<User> existingAdmin = findByUsername(adminUsername);
        if (existingAdmin.isPresent())
            return existingAdmin.get();

        User admin = new User(adminUsername, adminPassword, adminEmail,
                Collections.singleton(adminRole));

        return create(admin);
    }
}
