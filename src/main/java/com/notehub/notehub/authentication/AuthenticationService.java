package com.notehub.notehub.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notehub.notehub.authentication.dto.LoginDTO;
import com.notehub.notehub.authentication.dto.LoginResponceDTO;
import com.notehub.notehub.authentication.dto.RegisterDTO;
import com.notehub.notehub.role.Role;
import com.notehub.notehub.role.RoleNotFoundException;
import com.notehub.notehub.role.RoleService;
import com.notehub.notehub.security.TokenService;
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
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginResponceDTO loginUser(LoginDTO loginDTO) {

        try {
            Authentication auth = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            String token = tokenService.generateJWT(auth);
            User user = userService.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new InvalidCredentialsException(
                            "User for username '" + loginDTO.getUsername() + "' not found"));

            UserDTO convertedUser = userMapper.toDTO(user);

            return new LoginResponceDTO(convertedUser, token);

        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Entered user credentials are invalid");
        }
    }

    @Transactional
    public LoginResponceDTO registerUser(RegisterDTO registerDTO) {

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        Role userRole = roleService.findByAuthority("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("User role not found"));

        User user = new User(registerDTO.getUsername(), encodedPassword, registerDTO.getEmail());
        user.getRoles().add(userRole);

        userService.createUser(user);

        return loginUser(new LoginDTO(registerDTO.getUsername(), registerDTO.getPassword()));
    }

}
