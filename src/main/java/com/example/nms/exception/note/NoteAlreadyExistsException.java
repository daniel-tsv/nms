package com.example.nms.exception.note;

import com.example.nms.constants.MessageConstants;

public class NoteAlreadyExistsException extends RuntimeException {
    public NoteAlreadyExistsException(String title) {
        super(String.format(MessageConstants.NOTE_TITLE_EXISTS, title));
    }
}
