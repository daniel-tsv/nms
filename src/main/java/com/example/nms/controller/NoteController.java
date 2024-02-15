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
import com.example.nms.exception.note.InvalidNoteException;
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

	private static final String NOTE_WAS_NOT_FOUND = "The note %s was not found";

	@GetMapping("/{title}")
	public ResponseEntity<NoteDTO> findByTitle(@PathVariable("title") String title) {
		return ResponseEntity.ok(
				noteMapper.toDTO(
						noteService.findByTitleAndUser(title, authService.getAuthenticatedUser())
								.orElseThrow(
										() -> new NoteNotFoundException(String.format(NOTE_WAS_NOT_FOUND, title)))));
	}

	@GetMapping
	public ResponseEntity<List<NoteDTO>> findAll(
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") int size,
			@RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
			@RequestParam(name = "sort", required = false, defaultValue = "updatedAt") String sortBy) {

		return ResponseEntity.ok(
				noteMapper.toDTO(
						noteService.findNotesByUser(page, size, direction, sortBy, authService.getAuthenticatedUser())
								.toList()));
	}

	@PostMapping
	public ResponseEntity<Object> createNote(@RequestBody @Valid NoteDTO noteDTO, BindingResult br) {

		noteDTOValidator.validate(noteDTO, br);
		if (br.hasFieldErrors())
			throw new InvalidNoteException(
					br.getFieldErrors().stream()
							.map(err -> err.getField() + " - " + err.getDefaultMessage())
							.collect(Collectors.toList())
							.toString());

		return ResponseEntity.ok(
				noteMapper.toDTO(
						noteService.save(
								new Note(noteDTO.getTitle(), authService.getAuthenticatedUser()))));
	}

	@PatchMapping("/{title}")
	public ResponseEntity<Object> updateNote(@PathVariable("title") String title,
			@RequestBody @Valid NoteDTO noteDTO, BindingResult br) {

		Note existingNote = noteService.findByTitleAndUser(title, authService.getAuthenticatedUser())
				.orElseThrow(() -> new NoteNotFoundException(
						String.format(NOTE_WAS_NOT_FOUND, title)));

		noteDTO.setUuid(existingNote.getUuid());
		noteDTOValidator.validate(noteDTO, br);
		if (br.hasFieldErrors())
			throw new InvalidNoteException(
					br.getFieldErrors().stream()
							.map(err -> err.getField() + " - " + err.getDefaultMessage())
							.collect(Collectors.toList())
							.toString());

		return ResponseEntity.ok(
				noteMapper.toDTO(
						noteService.updateFromDTO(existingNote, noteDTO)));
	}

	@DeleteMapping("/{title}")
	public ResponseEntity<String> deleteNote(@PathVariable("title") String title) {
		if (noteService.deleteByTitleAndOwner(title, authService.getAuthenticatedUser()))
			ResponseEntity.ok(String.format("The note %s was successfully deleted", title));
		throw new NoteNotFoundException(String.format(NOTE_WAS_NOT_FOUND, title));
	}
}