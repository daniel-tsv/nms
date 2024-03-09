package com.example.nms.exception.user;

import com.example.nms.constants.MessageConstants;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException(String username) {
        super(String.format(MessageConstants.USERNAME_ALREADY_TAKEN, username));
    }
}
