package com.example.nms.controller;

import java.net.URI;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.dto.NoteSummaryDTO;
import com.example.nms.dto.NotesPageDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.exception.note.NoteValidationException;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.service.note.NoteService;
import com.example.nms.service.user.UserService;
import com.example.nms.validator.NoteDTOValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;
    private final UserService userService;
    private final NoteDTOValidator noteDTOValidator;

    @GetMapping("/title/{title}")
    public ResponseEntity<NoteDetailDTO> findByTitle(@PathVariable("title") String title) {

        Note note = noteService.findByTitleAndUser(title, userService.getAuthenticatedUser())
                .orElseThrow(() -> new NoteNotFoundException(title));

        return ResponseEntity.ok(noteMapper.toDetailDTO(note));
    }

    @GetMapping
    public ResponseEntity<NotesPageDTO> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "updatedAt") String sortBy) {

        PageRequest request = noteService.createPageRequest(page, size, direction, sortBy);
        Page<Note> notesPage = noteService.findAll(request, userService.getAuthenticatedUser());
        List<NoteSummaryDTO> noteSummaryDTOs = noteMapper.toSummaryDTO(notesPage.toList());

        NotesPageDTO notesPageDTO = new NotesPageDTO(noteSummaryDTOs, page, notesPage.getTotalElements(),
                notesPage.getTotalPages());

        return ResponseEntity.ok(notesPageDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<NotesPageDTO> searchNotes(
            @RequestParam(name = "term") String term,
            @RequestParam(name = "searchInContents", defaultValue = "false") boolean searchInContents,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "updatedAt") String sortBy) {

        PageRequest request = noteService.createPageRequest(page, size, direction, sortBy);
        Page<Note> notesPage = noteService.search(term, searchInContents, request, userService.getAuthenticatedUser());
        List<NoteSummaryDTO> noteSummaryDTOs = noteMapper.toSummaryDTO(notesPage.toList());

        NotesPageDTO notesPageDTO = new NotesPageDTO(noteSummaryDTOs, page, notesPage.getTotalElements(),
                notesPage.getTotalPages());

        return ResponseEntity.ok(notesPageDTO);
    }

    @PostMapping
    public ResponseEntity<Object> createNote(@RequestBody @Valid NoteDetailDTO noteDTO, BindingResult br) {

        noteDTOValidator.validate(noteDTO, br);
        if (br.hasErrors())
            throw new NoteValidationException(br);

        noteService.create(noteDTO, userService.getAuthenticatedUser());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/title/{title}")
                .buildAndExpand(noteDTO.getTitle()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/title/{title}")
    public ResponseEntity<NoteDetailDTO> updateNote(@PathVariable("title") String titleOfNoteToBeUpdated,
            @RequestBody @Valid NoteDetailDTO updatedNoteDTO, BindingResult br) {

        User user = userService.getAuthenticatedUser();
        Note existingNote = noteService.findByTitleAndUser(titleOfNoteToBeUpdated, user)
                .orElseThrow(() -> new NoteNotFoundException(titleOfNoteToBeUpdated));

        // set updatedNoteDTO UUID, for validator to work properly
        updatedNoteDTO.setUuid(existingNote.getUuid());
        noteDTOValidator.validate(updatedNoteDTO, br);
        if (br.hasErrors())
            throw new NoteValidationException(br);

        Note updatedNote = noteService.updateNoteDetails(titleOfNoteToBeUpdated, updatedNoteDTO, user);

        return ResponseEntity.ok(noteMapper.toDetailDTO(updatedNote));
    }

    @DeleteMapping("/title/{title}")
    public ResponseEntity<Void> deleteNote(@PathVariable("title") String title) {

        noteService.deleteByTitleAndUser(title, userService.getAuthenticatedUser());

        return ResponseEntity.noContent().build();
    }

}
