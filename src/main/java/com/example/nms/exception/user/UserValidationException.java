package com.example.nms.exception.user;

import org.springframework.validation.Errors;

import com.example.nms.util.ErrorUtils;

public class UserValidationException extends RuntimeException {

    public UserValidationException(Errors errors) {
        super(ErrorUtils.formatErrors(errors));
    }
}
