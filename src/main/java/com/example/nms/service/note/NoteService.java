package com.example.nms.service.note;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;

public interface NoteService {

    Optional<Note> findById(UUID id);

    Optional<Note> findByTitleAndUser(String title, User owner);

    Page<Note> findAll(Pageable pageable, User owner);

    Page<Note> search(String term, boolean searchInContents, Pageable pageable, User user);

    Note create(NoteDetailDTO noteDTO, User user);

    Note updateNoteDetails(String title, NoteDetailDTO updatedNoteDTO, User user);

    void deleteByTitleAndUser(String title, User owner);

    PageRequest createPageRequest(int page, int size, String direction, String sortBy);

    int getUserNotesCount(User user);
}
