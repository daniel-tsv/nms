package com.example.nms.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.Role;
import com.example.nms.entity.User;
import com.example.nms.exception.role.RoleNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.security.UserDetailsImpl;
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
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User for username '" + loginDTO.getUsername() + "' not found"));
            UserDTO userDTO = userMapper.toDTO(user);

            return new AuthResponseDTO(userDTO, token);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Entered user credentials are not valid");
        } catch (LockedException e) {
            throw new LockedException("Your account has been locked");
        } catch (DisabledException e) {
            throw new DisabledException("Your account has been disabled");
        }
    }

    @Override
    @Transactional
    public AuthResponseDTO registerUser(RegisterDTO registerDTO) {

        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        Role userRole = roleService.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("User role not found"));

        User user = new User(registerDTO.getUsername(), encodedPassword, registerDTO.getEmail());
        user.getRoles().add(userRole);
        userService.save(user);

        return loginUser(new LoginDTO(registerDTO.getUsername(), registerDTO.getPassword()));
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userDetails.getUser();
    }

}
