package com.example.nms.exception.note;

import org.springframework.validation.Errors;

import com.example.nms.util.ErrorUtils;

public class NoteValidationException extends RuntimeException {

    public NoteValidationException(Errors errors) {
        super(ErrorUtils.formatErrors(errors));
    }
}
