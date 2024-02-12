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

    Page<Note> listNotes(int page, int size, String direction, String sortBy);

    Note save(Note note);

    Note update(UUID noteId, Note note);

    void deleteByTitleAndOwner(String title, User owner);

    int countUserNotes(User user);

    Note updateEntityFromDTO(UUID noteUuid, NoteDTO noteDTO);
}
