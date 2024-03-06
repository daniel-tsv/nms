package com.example.nms.service.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;

import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;

public interface NoteService {
    Optional<Note> findById(UUID id);

    Optional<Note> findByTitleAndUser(String title, User owner);

    Page<Note> findUserNotes(Pageable pageable, User owner);

    Page<Note> searchNotes(String term, boolean searchInContents, Pageable pageable, User user);

    Note createNote(NoteDetailDTO noteDTO, User user, Errors errors);

    Note updateNoteDetails(String title, NoteDetailDTO updatedNoteDTO, Errors errors, User user);

    void deleteByTitleAndOwner(String title, User owner);

    PageRequest createPageRequestOf(int page, int size, String direction, String sortBy);
}
