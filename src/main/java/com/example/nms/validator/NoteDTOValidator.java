package com.example.nms.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.nms.dto.NoteDTO;
import com.example.nms.service.auth.AuthService;
import com.example.nms.service.note.NoteService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoteDTOValidator implements Validator {

    private final NoteService noteService;
    private final AuthService authService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return NoteDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        NoteDTO noteDTO = (NoteDTO) target;

        noteService.findByTitleAndUser(noteDTO.getTitle(), authService.getAuthenticatedUser()).ifPresent(n -> {
            if (noteDTO.getUuid() == null || !n.getUuid().equals(noteDTO.getUuid()))
                errors.rejectValue("title", "note.title.exists",
                        "Note with title '" + noteDTO.getTitle() + "' already exists");
        });

    }
}
