package com.example.nms.validator;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.repository.NoteRepository;
import com.example.nms.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoteDTOValidator implements Validator {

    private final NoteRepository noteRepository;
    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return NoteDetailDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {

        NoteDetailDTO noteDTO = (NoteDetailDTO) target;

        noteRepository.findByTitleAndUser(noteDTO.getTitle(), userService.getAuthenticatedUser()).ifPresent(n -> {
            if (!n.getUuid().equals(noteDTO.getUuid()))
                errors.rejectValue("title", "note.title.exists",
                        String.format(MessageConstants.NOTE_TITLE_EXISTS, noteDTO.getTitle()));
        });

    }
}
