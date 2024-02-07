package com.notehub.notehub.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notehub.notehub.dto.AuthResponseDTO;
import com.notehub.notehub.dto.LoginDTO;
import com.notehub.notehub.dto.RegisterDTO;
import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.entities.Role;
import com.notehub.notehub.entities.User;
import com.notehub.notehub.exceptions.role.RoleNotFoundException;
import com.notehub.notehub.mappers.UserMapper;
import com.notehub.notehub.services.role.RoleService;
import com.notehub.notehub.services.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthResponseDTO loginUser(LoginDTO loginDTO) {

        try {
            Authentication auth = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            String token = tokenService.generateJWT(auth);
            User user = userService.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User for username '" + loginDTO.getUsername() + "' not found"));

            UserDTO convertedUser = userMapper.toDTO(user);

            return new AuthResponseDTO(convertedUser, token);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Entered user credentials are not valid");
        }
    }

    @Transactional
    public AuthResponseDTO registerUser(RegisterDTO registerDTO) {

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        Role userRole = roleService.findByAuthority("USER")
                .orElseThrow(() -> new RoleNotFoundException("User role not found"));

        User user = new User(registerDTO.getUsername(), encodedPassword, registerDTO.getEmail());
        user.getRoles().add(userRole);

        userService.createUser(user);

        return loginUser(new LoginDTO(registerDTO.getUsername(), registerDTO.getPassword()));
    }

}
