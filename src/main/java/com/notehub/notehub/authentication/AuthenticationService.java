package com.notehub.notehub.authentication;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notehub.notehub.authentication.dto.LoginDTO;
import com.notehub.notehub.authentication.dto.RegisterDTO;
import com.notehub.notehub.role.Role;
import com.notehub.notehub.role.RoleNotFoundException;
import com.notehub.notehub.role.RoleService;
import com.notehub.notehub.user.User;
import com.notehub.notehub.user.UserDTO;
import com.notehub.notehub.user.UserMapper;
import com.notehub.notehub.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public UserDTO loginUser(LoginDTO loginDTO) {

        User user = userService.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(loginDTO.getUsername()));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
            throw new WrongCredentialsException("Invalid user credentials");

        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO registerUser(RegisterDTO registerDTO) {

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        Role userRole = roleService.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("User role not found"));

        User user = new User(registerDTO.getUsername(), encodedPassword, registerDTO.getEmail());
        user.getRoles().add(userRole);

        return userMapper.toDTO(userService.createUser(user));
    }

}
