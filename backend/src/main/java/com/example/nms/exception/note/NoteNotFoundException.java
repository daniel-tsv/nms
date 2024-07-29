package com.example.nms.exception.note;

import com.example.nms.constants.MessageConstants;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String title) {
        super(String.format(MessageConstants.NOTE_NOT_FOUND, title));
    }
}