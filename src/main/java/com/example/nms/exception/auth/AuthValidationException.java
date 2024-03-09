package com.example.nms.exception.auth;

import org.springframework.validation.Errors;

import com.example.nms.util.ErrorUtils;

public class AuthValidationException extends RuntimeException {

    public AuthValidationException(Errors errors) {
        super(ErrorUtils.formatErrors(errors));
    }
}
