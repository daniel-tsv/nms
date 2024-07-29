package com.example.nms.exception.role;

import org.springframework.validation.Errors;

import com.example.nms.util.ErrorUtils;

public class RoleValidationException extends RuntimeException {

    public RoleValidationException(Errors errors) {
        super(ErrorUtils.formatErrors(errors));
    }
}
