package com.example.nms.controller;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.dto.NoteSummaryDTO;
import com.example.nms.entity.Note;
import com.example.nms.exception.note.InvalidNoteException;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.service.note.NoteService;
import com.example.nms.service.user.UserService;
import com.example.nms.validator.NoteDTOValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

	private final NoteService noteService;
	private final NoteMapper noteMapper;
	private final UserService userService;
	private final NoteDTOValidator noteDTOValidator;

	@GetMapping("/title/{title}")
	public ResponseEntity<NoteDetailDTO> findByTitle(@PathVariable("title") String title) {
		return ResponseEntity.ok(
				noteMapper.toDetailDTO(
						noteService.findByTitleAndUser(title, userService.getAuthenticatedUser())
								.orElseThrow(
										() -> new NoteNotFoundException(
												String.format(MessageConstants.NOTE_NOT_FOUND, title)))));
	}

	@GetMapping
	public ResponseEntity<List<NoteSummaryDTO>> findAll(
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") int size,
			@RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
			@RequestParam(name = "sort", required = false, defaultValue = "updatedAt") String sortBy) {

		return ResponseEntity.ok(
				noteMapper.toSummaryDTO(
						noteService
								.findUserNotes(
										noteService.createPageRequestOf(page, size, direction, sortBy),
										userService.getAuthenticatedUser())
								.toList()));
	}

	@GetMapping("/search")
	public ResponseEntity<List<NoteSummaryDTO>> searchNotes(
			@RequestParam(name = "term", required = true) String term,
			@RequestParam(name = "searchInContents", required = false, defaultValue = "false") boolean searchInContents,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "20") int size,
			@RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
			@RequestParam(name = "sort", required = false, defaultValue = "updatedAt") String sortBy) {

		return ResponseEntity.ok(
				noteMapper.toSummaryDTO(
						noteService.searchNotes(term, searchInContents,
								noteService.createPageRequestOf(page, size, direction, sortBy),
								userService.getAuthenticatedUser()).toList()));
	}

	@PostMapping
	public ResponseEntity<Object> createNote(@RequestBody @Valid NoteDetailDTO noteDTO, BindingResult br) {

		noteDTOValidator.validate(noteDTO, br);
		if (br.hasFieldErrors())
			throw new InvalidNoteException(
					br.getFieldErrors().stream()
							.map(err -> err.getField() + " - " + err.getDefaultMessage())
							.collect(Collectors.toList())
							.toString());

		noteService.save(
				new Note(noteDTO.getTitle(), userService.getAuthenticatedUser(), noteDTO.getContents()));

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/title/{title}")
				.buildAndExpand(noteDTO.getTitle()).toUri();

		return ResponseEntity.created(location).build();
	}

	@PatchMapping("/title/{title}")
	public ResponseEntity<NoteDetailDTO> updateNote(@PathVariable("title") String title,
			@RequestBody @Valid NoteDetailDTO noteDTO, BindingResult br) {

		Note existingNote = noteService.findByTitleAndUser(title, userService.getAuthenticatedUser())
				.orElseThrow(() -> new NoteNotFoundException(
						String.format(MessageConstants.NOTE_NOT_FOUND, title)));
		noteDTO.setUuid(existingNote.getUuid());

		noteDTOValidator.validate(noteDTO, br);
		if (br.hasFieldErrors())
			throw new InvalidNoteException(
					br.getFieldErrors().stream()
							.map(err -> err.getField() + " - " + err.getDefaultMessage())
							.collect(Collectors.toList())
							.toString());

		return ResponseEntity.ok(
				noteMapper.toDetailDTO(
						noteService.updateFromDTO(existingNote, noteDTO)));
	}

	@DeleteMapping("/title/{title}")
	public ResponseEntity<Void> deleteNote(@PathVariable("title") String title) {
		if (noteService.deleteByTitleAndOwner(title, userService.getAuthenticatedUser()))
			return ResponseEntity.noContent().build();
		throw new NoteNotFoundException(String.format(MessageConstants.NOTE_NOT_FOUND, title));
	}

}
