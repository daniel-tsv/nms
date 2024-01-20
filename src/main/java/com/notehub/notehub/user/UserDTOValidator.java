package com.notehub.notehub.user;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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

        userService.findByUsername(userDTO.getUsername()).ifPresent(user -> {
            if (!user.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("username", "user.username.exists", "This username has already been taken");
        });

        userService.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            if (!user.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("email", "user.email.exists", "This email has already been taken");
        });

        if (userDTO.getUsername().equals(userDTO.getPassword()))
            errors.rejectValue("password", "password.username.same", "Password and username cannot be same");

        if (userDTO.getEmail().equals(userDTO.getPassword()))
            errors.rejectValue("password", "password.email.same", "Password and email cannot be same");

    }
}
