package com.example.nms.service.note;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nms.dto.NoteDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.repository.NoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public Optional<Note> findById(UUID id) {
        return noteRepository.findById(id);
    }

    @Override
    public Optional<Note> findByTitleAndUser(String title, User owner) {
        return noteRepository.findByTitleAndUser(title, owner);
    }

    @Override
    public Page<Note> listNotesByUser(int page, int size, String direction, String sortBy, User owner) {

        page = Math.max(page, 0);
        size = Math.max(size, 1);
        sortBy = Arrays.asList("title", "createdAt", "updatedAt").contains(sortBy) ? sortBy : "updatedAt";
        Sort.Direction sortDirection = Sort.Direction.ASC.name().equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return noteRepository.findAllByUser(PageRequest.of(page, size, sort), owner);
    }

    @Override
    @Transactional
    public Note save(Note note) {
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note update(UUID noteId, Note updatedNote) {
        updatedNote.setUuid(noteId);
        return noteRepository.save(updatedNote);
    }

    @Override
    @Transactional
    public boolean deleteByTitleAndOwner(String title, User owner) {
        if (noteRepository.existsByTitleAndUser(title, owner)) {
            noteRepository.deleteByTitleAndUser(title, owner);
            return true;
        }
        return false;
    }

    @Override
    public int countUserNotes(User user) {
        return noteRepository.countByUser(user);
    }

    @Override
    @Transactional
    public Note updateEntityFromDTO(Note existingNote, NoteDTO updatedNoteDTO) {

        Note updatedNote = noteMapper.toEntity(updatedNoteDTO);

        updatedNote.setContents(existingNote.getContents());
        updatedNote.setCreatedAt(existingNote.getCreatedAt());
        updatedNote.setUser(existingNote.getUser());
        updatedNote.setUuid(existingNote.getUuid());

        return noteRepository.save(updatedNote);
    }
}