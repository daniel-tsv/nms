package com.notehub.notehub.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.service.auth.AuthService;
import com.notehub.notehub.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDTOValidator implements Validator {

    private final UserService userService;
    private final AuthService authService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        UserDTO userDTO = (UserDTO) target;
        userDTO.setUuid(authService.getAuthenticatedUser().getUuid());

        userService.findByUsername(userDTO.getUsername()).ifPresent(u -> {
            if (!u.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("username", "user.username.exists",
                        "Username '" + u.getUsername() + "' has already been taken");
        });

        userService.findByEmail(userDTO.getEmail()).ifPresent(u -> {
            if (!u.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("email", "user.email.exists", "Email '" + u.getEmail() + "' has already been taken");
        });
    }
}
