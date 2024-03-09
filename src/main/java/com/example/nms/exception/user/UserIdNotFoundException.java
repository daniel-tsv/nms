package com.example.nms.exception.user;

import java.util.UUID;

import com.example.nms.constants.MessageConstants;

public class UserIdNotFoundException extends RuntimeException {

    public UserIdNotFoundException(UUID id) {
        super(String.format(MessageConstants.USER_ID_NOT_FOUND, id));
    }
}
