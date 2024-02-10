package com.notehub.notehub.validators;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.notehub.notehub.dto.UserDTO;
import com.notehub.notehub.services.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDTOValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        userService.findByUsername(userDTO.getUsername()).ifPresent(u -> {
            if (!u.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("username", "user.username.exists", "This username has already been taken");
        });

        userService.findByEmail(userDTO.getEmail()).ifPresent(u -> {
            if (!u.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("email", "user.email.exists", "This email has already been taken");
        });
    }
}
