package com.notehub.notehub.validators;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.notehub.notehub.services.note.NoteService;

import entities.Note;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoteValidator implements Validator {

    private final NoteService noteService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Note.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Note note = (Note) target;

        noteService.findByTitle(note.getTitle()).ifPresent(n -> {
            if (!n.getUuid().equals(note.getUuid()))
                errors.rejectValue("title", "note.title.exists", "Note with this title already exists");
        });
    }

}
