package com.example.nms.exception.role;

import com.example.nms.constants.MessageConstants;

public class RoleNameNotFoundException extends RuntimeException {

    public RoleNameNotFoundException(String name) {
        super(String.format(MessageConstants.ROLE_NOT_FOUND, name));
    }
}
