package com.notehub.notehub.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public Optional<Note> findById(UUID id) {
        return noteRepository.findById(id);
    }

    @Override
    public Optional<Note> findByTitle(String title) {
        return noteRepository.findByTitle(title);
    }

    @Override
    public Page<Note> listNotes(int page, int size, String direction, String sortBy) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return noteRepository.findAll(PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional
    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note updateNote(UUID id, Note updatedNote) {
        updatedNote.setUuid(id);

        return noteRepository.save(updatedNote);
    }

    @Override
    @Transactional
    public void deleteNote(UUID id) {
        noteRepository.deleteById(id);
    }
}