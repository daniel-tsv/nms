package com.notehub.notehub.validators;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.notehub.notehub.services.user.UserService;

import entities.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;

        userService.findByUsername(user.getUsername()).ifPresent(u -> {
            if (!u.getUuid().equals(user.getUuid()))
                errors.rejectValue("username", "user.username.exists", "This username has already been taken");
        });

        userService.findByEmail(user.getEmail()).ifPresent(u -> {
            if (!u.getUuid().equals(user.getUuid()))
                errors.rejectValue("email", "user.email.exists", "This email has already been taken");
        });

        if (user.getUsername().equals(user.getPassword()))
            errors.rejectValue("password", "password.username.same", "Password and username cannot be same");

        if (user.getEmail().equals(user.getPassword()))
            errors.rejectValue("password", "password.email.same", "Password and email cannot be same");

    }
}
