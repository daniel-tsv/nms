package com.example.nms.service.note;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.exception.note.NoteValidationException;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.repository.NoteRepository;
import com.example.nms.validator.NoteDTOValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final NoteDTOValidator noteDTOValidator;

    @Override
    public Optional<Note> findById(UUID id) {
        return noteRepository.findById(id);
    }

    @Override
    public Optional<Note> findByTitleAndUser(String title, User owner) {
        return noteRepository.findByTitleAndUser(title, owner);
    }

    @Override
    public Page<Note> findUserNotes(Pageable pageable, User owner) {
        return noteRepository.findAllByUser(pageable, owner);
    }

    @Override
    public Page<Note> searchNotes(String term, boolean searchInContents, Pageable pageable, User user) {

        if (term == null || term.isBlank())
            throw new IllegalArgumentException(MessageConstants.NOTE_SEARCH_TERM_EMPTY);

        return searchInContents
                ? noteRepository.findByContentsContainingIgnoreCaseAndUser(term, pageable, user)
                : noteRepository.findByTitleContainingIgnoreCaseAndUser(term, pageable, user);
    }

    // TODO test
    @Override
    @Transactional
    public Note createNote(NoteDetailDTO noteDTO, User user, Errors errors) {

        noteDTOValidator.validate(noteDTO, errors);
        if (errors.hasErrors())
            throw new NoteValidationException(errors);

        Note note = new Note(noteDTO.getTitle(), user, noteDTO.getContents());
        noteRepository.save(note);

        return noteRepository.save(note);
    }

    // TODO test
    @Override
    @Transactional
    public Note updateNoteDetails(String title, NoteDetailDTO updatedNoteDTO, Errors errors, User user) {

        Note existingNote = noteRepository.findByTitleAndUser(title, user)
                .orElseThrow(() -> new NoteNotFoundException(title));

        updatedNoteDTO.setUuid(existingNote.getUuid());
        noteDTOValidator.validate(updatedNoteDTO, errors);
        if (errors.hasErrors())
            throw new NoteValidationException(errors);

        if (updatedNoteDTO.getContents() == null || updatedNoteDTO.getContents().isBlank())
            updatedNoteDTO.setContents(existingNote.getContents());

        Note mappedNote = noteMapper.toEntity(updatedNoteDTO);
        mappedNote.setCreatedAt(existingNote.getCreatedAt());
        mappedNote.setUser(existingNote.getUser());
        mappedNote.setUuid(existingNote.getUuid());

        return noteRepository.save(mappedNote);
    }

    @Override
    @Transactional
    public void deleteByTitleAndOwner(String title, User owner) {
        if (!noteRepository.existsByTitleAndUser(title, owner))
            throw new NoteNotFoundException(title);

        noteRepository.deleteByTitleAndUser(title, owner);
    }

    @Override
    public PageRequest createPageRequestOf(int page, int size, String direction, String sortBy) {
        page = Math.max(page, 0);
        size = Math.max(size, 1);
        sortBy = Arrays.asList("title", "createdAt", "updatedAt").contains(sortBy) ? sortBy : "updatedAt";
        Sort.Direction sortDirection = Sort.Direction.ASC.name().equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);

        return PageRequest.of(page, size, sort);
    }

}