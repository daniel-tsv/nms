package com.example.nms.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.example.nms.dto.NoteDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.service.auth.AuthService;
import com.example.nms.service.note.NoteService;
import com.example.nms.validator.NoteDTOValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;
    private final AuthService authService;
    private final NoteDTOValidator noteDTOValidator;

    @GetMapping("/{title}")
    public ResponseEntity<NoteDTO> findByTitle(@PathVariable("title") String title) {

        Note note = noteService.findByTitleAndUser(title, authService.getAuthenticatedUser())
                .orElseThrow(() -> new NoteNotFoundException("Note with title '" + title + "' has not been found"));

        return ResponseEntity.ok(noteMapper.toDTO(note));
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "sort", required = false, defaultValue = "uuid") String sortBy) {
        // TODO return user notes
        User user = authService.getAuthenticatedUser();

        throw new UnsupportedOperationException("Unimplemented method");
    }

    @PostMapping
    public ResponseEntity<Object> createNote(@RequestBody @Valid NoteDTO noteDTO, BindingResult br) {

        noteDTOValidator.validate(noteDTO, br);
        if (br.hasFieldErrors())
            return ResponseEntity.badRequest().body(br.getFieldErrors().stream()
                    .map(err -> err.getField() + " - " + err.getDefaultMessage()).collect(Collectors.toList()));

        Note note = new Note(noteDTO.getTitle(), authService.getAuthenticatedUser());
        Note createdNote = noteService.save(note);

        return ResponseEntity.ok(noteMapper.toDTO(createdNote));
    }

    // TODO test
    @PatchMapping("/{title}")
    public ResponseEntity<Object> updateNote(@PathVariable("title") String title,
            @RequestBody @Valid NoteDTO noteDTO, BindingResult br) {

        User user = authService.getAuthenticatedUser();
        Note existingNote = noteService.findByTitleAndUser(title, user)
                .orElseThrow(() -> new NoteNotFoundException("Note with title '" + title + "' does not exists"));

        noteDTO.setUuid(existingNote.getUuid());
        noteDTOValidator.validate(noteDTO, br);
        if (br.hasFieldErrors())
            return ResponseEntity.badRequest().body(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .toList());

        Note updatedNote = noteService.updateEntityFromDTO(existingNote.getUuid(), noteDTO);

        return ResponseEntity.ok(noteMapper.toDTO(updatedNote));
    }

    // TODO test
    @DeleteMapping("/{title}")
    public ResponseEntity<String> deleteNote(@PathVariable("title") String title) {
        User user = authService.getAuthenticatedUser();
        noteService.deleteByTitleAndOwner(title, user);
        // todo check if deletion was successful
        return ResponseEntity.ok("Note '" + title + "' has been successfully deleted");
    }
}
