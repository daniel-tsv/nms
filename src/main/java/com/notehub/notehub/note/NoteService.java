package com.notehub.notehub.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

public interface NoteService {
    Optional<Note> findById(UUID id);

    Optional<Note> findByTitle(String title);

    Page<Note> listNotes(int page, int size, String direction, String sortBy);

    Note createNote(Note note);

    Note updateNote(UUID id, Note note);

    void deleteNote(UUID id);
}
