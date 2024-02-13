package com.example.nms.service.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.nms.dto.NoteDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;

public interface NoteService {
    Optional<Note> findById(UUID id);

    Optional<Note> findByTitleAndUser(String title, User owner);

    Page<Note> findNotesByUser(int page, int size, String direction, String sortBy, User owner);

    Note save(Note note);

    Note updateFromDTO(Note existingNote, NoteDTO updatedNoteDTO);

    boolean deleteByTitleAndOwner(String title, User owner);

    int countUserNotes(User user);

}
