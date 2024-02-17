package com.example.nms.service.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;

public interface NoteService {
    Optional<Note> findById(UUID id);

    Optional<Note> findByTitleAndUser(String title, User owner);

    Page<Note> findUserNotes(Pageable pageable, User owner);

    Page<Note> searchNotes(String term, boolean searchInContents, Pageable pageable, User user);

    int getUserNotesCount(User user);

    Note save(Note note);

    Note updateFromDTO(Note existingNote, NoteDetailDTO updatedNoteDTO);

    boolean deleteByTitleAndOwner(String title, User owner);

    PageRequest createPageRequestOf(int page, int size, String direction, String sortBy);
}
