package com.example.nms.controller;

import java.net.URI;
import java.util.List;

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
import com.example.nms.entity.Note;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.service.note.NoteService;
import com.example.nms.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;
    private final UserService userService;

    @GetMapping("/title/{title}")
    public ResponseEntity<NoteDetailDTO> findByTitle(@PathVariable("title") String title) {

        Note note = noteService.findByTitleAndUser(title, userService.getAuthenticatedUser())
                .orElseThrow(() -> new NoteNotFoundException(title));

        return ResponseEntity.ok(noteMapper.toDetailDTO(note));
    }

    @GetMapping
    public ResponseEntity<List<NoteSummaryDTO>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "updatedAt") String sortBy) {

        PageRequest request = noteService.createPageRequestOf(page, size, direction, sortBy);
        List<Note> notes = noteService.findUserNotes(request, userService.getAuthenticatedUser()).toList();

        return ResponseEntity.ok(noteMapper.toSummaryDTO(notes));
    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteSummaryDTO>> searchNotes(
            @RequestParam(name = "term") String term,
            @RequestParam(name = "searchInContents", defaultValue = "false") boolean searchInContents,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "updatedAt") String sortBy) {

        PageRequest request = noteService.createPageRequestOf(page, size, direction, sortBy);
        List<Note> notes = noteService.searchNotes(term, searchInContents, request, userService.getAuthenticatedUser())
                .toList();

        return ResponseEntity.ok(noteMapper.toSummaryDTO(notes));
    }

    @PostMapping
    public ResponseEntity<Object> createNote(@RequestBody @Valid NoteDetailDTO noteDTO, BindingResult br) {

        noteService.createNote(noteDTO, userService.getAuthenticatedUser(), br);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/title/{title}")
                .buildAndExpand(noteDTO.getTitle()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/title/{title}")
    public ResponseEntity<NoteDetailDTO> updateNote(@PathVariable("title") String title,
            @RequestBody @Valid NoteDetailDTO updatedNoteDTO, BindingResult br) {

        Note updatedNote = noteService.updateNoteDetails(title, updatedNoteDTO, br, userService.getAuthenticatedUser());

        return ResponseEntity.ok(noteMapper.toDetailDTO(updatedNote));
    }

    @DeleteMapping("/title/{title}")
    public ResponseEntity<Void> deleteNote(@PathVariable("title") String title) {
        noteService.deleteByTitleAndOwner(title, userService.getAuthenticatedUser());
        return ResponseEntity.noContent().build();
    }

}
