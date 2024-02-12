package com.example.nms.service.note;

import java.time.Instant;
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
import com.example.nms.exception.note.NoteNotFoundException;
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
    public Page<Note> listNotes(int page, int size, String direction, String sortBy) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return noteRepository.findAll(PageRequest.of(page, size, sort));
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
    public void deleteByTitleAndOwner(String title, User owner) {
        noteRepository.deleteByTitleAndUser(title, owner);
    }

    @Override
    public int countUserNotes(User user) {
        return noteRepository.countByUser(user);
    }

    @Override
    @Transactional
    public Note updateEntityFromDTO(UUID noteUuid, NoteDTO updatedNoteDTO) {

        Note existingNote = findById(noteUuid)
                .orElseThrow(
                        () -> new NoteNotFoundException("Unable to update note - id '" + noteUuid + "' was not found"));
        Note updatedNote = noteMapper.toEntity(updatedNoteDTO);

        // todo remove
        System.out.println("updatedNote title:" + updatedNote.getTitle());

        updatedNote.setContents(existingNote.getContents());
        updatedNote.setCreatedAt(existingNote.getCreatedAt());
        updatedNote.setUpdatedAt(Instant.now());
        updatedNote.setUser(existingNote.getUser());
        updatedNote.setUuid(existingNote.getUuid());

        return noteRepository.save(updatedNote);
    }

    // todo
    // @Override
    // public List<Note> listUserNotes(User user) {
    //     return noteRepository.findByUser(user);
    // }
}