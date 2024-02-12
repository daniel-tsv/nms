package com.example.nms.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.nms.dto.NoteDTO;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.service.note.NoteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @GetMapping("/{title}")
    public ResponseEntity<NoteDTO> findByTitle(@PathVariable("title") String title) {
        //return ResponseEntity.ok(noteMapper.toDTO(noteService.findByTitle(title).orElseThrow(() -> new NoteNotFoundException("Note with this title does not exist"))));
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "sort", required = false, defaultValue = "uuid") String sortBy) {

        throw new UnsupportedOperationException("Unimplemented method");
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote() {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable("id") UUID id) {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable("id") UUID id) {
        throw new UnsupportedOperationException("Unimplemented method");
    }
}
