package com.example.nms.service.auth;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.constants.MessageConstants;
import com.example.nms.constants.RoleConstants;
import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.exception.role.RoleNameNotFoundException;
import com.example.nms.exception.user.UserNameNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.security.jwt.JWTUtil;
import com.example.nms.service.role.RoleService;
import com.example.nms.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtProvider;

    @Override
    public AuthResponseDTO loginUser(LoginDTO loginDTO) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            String token = jwtProvider.generateJWT(auth);
            User user = userService.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new UserNameNotFoundException(loginDTO.getUsername()));
            UserDTO userDTO = userMapper.toDTO(user);

            return new AuthResponseDTO(userDTO, token);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(MessageConstants.AUTH_INVALID_CREDENTIALS);
        } catch (LockedException e) {
            throw new LockedException(MessageConstants.AUTH_ACCOUNT_LOCKED);
        } catch (DisabledException e) {
            throw new DisabledException(MessageConstants.AUTH_ACCOUNT_DISABLED);
        }
    }

    @Override
    @Transactional
    public AuthResponseDTO registerUser(RegisterDTO registerDTO) {

        Role userRole = roleService.findByName(RoleConstants.ROLE_USER)
                .orElseThrow(() -> new RoleNameNotFoundException(RoleConstants.ROLE_USER));

        User user = new User(registerDTO.getUsername(), passwordEncoder.encode(registerDTO.getPassword()),
                registerDTO.getEmail(), Collections.singleton(userRole));
        userService.create(user);

        return loginUser(new LoginDTO(registerDTO.getUsername(), registerDTO.getPassword()));
    }

}
