package com.example.nms.exception.user;

import com.example.nms.constants.MessageConstants;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super(String.format(MessageConstants.EMAIL_ALREADY_TAKEN, email));
    }
}
