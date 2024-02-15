package com.example.nms.service.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;

public interface NoteService {
    Optional<Note> findById(UUID id);

    Optional<Note> findByTitleAndUser(String title, User owner);

    Page<Note> findNotesByUser(int page, int size, String direction, String sortBy, User owner);

    Note save(Note note);

    Note updateFromDTO(Note existingNote, NoteDetailDTO updatedNoteDTO);

    boolean deleteByTitleAndOwner(String title, User owner);

    int getUserNotesCount(User user);

}
