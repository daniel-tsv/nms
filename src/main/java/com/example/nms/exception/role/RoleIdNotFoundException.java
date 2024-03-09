package com.example.nms.exception.role;

import java.util.UUID;

import com.example.nms.constants.MessageConstants;

public class RoleIdNotFoundException extends RuntimeException {

    public RoleIdNotFoundException(UUID id) {
        super(String.format(MessageConstants.ROLE_ID_NOT_FOUND, id));
    }
}
