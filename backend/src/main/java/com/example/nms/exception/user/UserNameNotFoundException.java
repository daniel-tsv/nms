package com.example.nms.exception.user;

import com.example.nms.constants.MessageConstants;

public class UserNameNotFoundException extends RuntimeException {

    public UserNameNotFoundException(String username) {
        super(String.format(MessageConstants.USER_USERNAME_NOT_FOUND, username));
    }
}