package com.notehub.notehub.note.controllers;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.notehub.notehub.note.Note;
import com.notehub.notehub.note.NoteDTO;
import com.notehub.notehub.note.NoteDTOValidator;
import com.notehub.notehub.note.NoteMapper;
import com.notehub.notehub.note.NoteService;
import com.notehub.notehub.note.exceptions.InvalidNoteException;
import com.notehub.notehub.note.exceptions.NoteAlreadyExistsException;
import com.notehub.notehub.note.exceptions.NoteNotFoundException;
import com.notehub.notehub.util.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/notes")
@RequiredArgsConstructor
public class NoteRestController {

    private final NoteService noteService;
    private final NoteDTOValidator noteDTOValidator;
    private final NoteMapper noteMapper;

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> findByUUID(@PathVariable UUID id) {
        return ResponseEntity.ok(noteMapper.toDTO(noteService.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note with this id does not exist"))));
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "sort", required = false, defaultValue = "uuid") String sortBy) {

        List<Note> notes = noteService.listNotes(page, size, direction, sortBy).getContent();

        return ResponseEntity.ok(noteMapper.toDTO(notes));
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@RequestBody @Validated NoteDTO noteDTO, BindingResult br) {

        noteDTOValidator.validate(noteDTO, br);

        if (br.hasErrors())
            throw new InvalidNoteException(
                    "note is not valid: " + br.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; ")));

        Note note = noteMapper.toEntity(noteDTO);
        Note createdNote = noteService.createNote(note);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdNote.getUuid()).toUri();

        return ResponseEntity.created(location).body(noteMapper.toDTO(createdNote));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable UUID id, @RequestBody @Validated NoteDTO noteDTO,
            BindingResult br) {

        noteDTOValidator.validate(noteDTO, br);

        if (br.hasErrors())
            throw new InvalidNoteException(
                    "note is not valid: " + br.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; ")));

        Note note = noteMapper.toEntity(noteDTO);
        Note updatedNote = noteService.updateNote(id, note);

        return ResponseEntity.ok(noteMapper.toDTO(updatedNote));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable UUID id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok("Successfully deleted note with id: {id}");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse noteNotFoundHandler(NoteNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND, "Note not found", ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse noteAlreadyExists(NoteAlreadyExistsException ex, HttpServletRequest request) {
        return new ErrorResponse(Instant.now(), HttpStatus.CONFLICT, "Note with this title already exists",
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidNoteException(InvalidNoteException ex, HttpServletRequest request) {
        return new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST, "Invalid note data",
                ex.getMessage(),
                request.getRequestURI());
    }
}
