package com.example.nms.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDTOValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserDTO.class.equals(clazz) || RegisterDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        UserDTO userDTO;

        if (target instanceof UserDTO) {
            userDTO = (UserDTO) target;
        } else {
            RegisterDTO register = (RegisterDTO) target;
            userDTO = new UserDTO(register.getUsername(), register.getEmail());
        }

        userService.findByUsername(userDTO.getUsername()).ifPresent(u -> {
            if (!u.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("username", "user.username.exists",
                        String.format(MessageConstants.USERNAME_ALREADY_TAKEN, u.getUsername()));
        });

        userService.findByUsername(userDTO.getEmail()).ifPresent(u -> {
            if (!u.getUuid().equals(userDTO.getUuid()))
                errors.rejectValue("email", "user.email.exists",
                        String.format(MessageConstants.EMAIL_ALREADY_TAKEN, u.getEmail()));
        });

    }
}
