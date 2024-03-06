package com.example.nms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;

import com.example.nms.constants.RoleConstants;
import com.example.nms.dto.NoteDetailDTO;
import com.example.nms.dto.NoteSummaryDTO;
import com.example.nms.entity.Note;
import com.example.nms.entity.User;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.exception.note.NoteValidationException;
import com.example.nms.mapper.NoteMapper;
import com.example.nms.service.note.NoteService;
import com.example.nms.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
class NoteControllerTest {

    @MockBean
    private NoteMapper noteMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private NoteService noteService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Faker faker;

    private User exampleUser;
    private Note exampleNote;
    private NoteDetailDTO exampleNoteDetail;
    private List<NoteSummaryDTO> exampleNoteSummaries;

    @BeforeEach
    void setUp() {
        exampleUser = new User();
        exampleNote = new Note();
        exampleNoteDetail = createNoteDetail();

        exampleNoteSummaries = IntStream.range(0, 5)
                .mapToObj(i -> createNoteSummary()).toList();
    }

    private NoteDetailDTO createNoteDetail() {
        return new NoteDetailDTO(UUID.randomUUID(), faker.lorem().word(),
                faker.lorem().sentence(10),
                faker.date().past(faker.random().nextInt(15, 30), TimeUnit.DAYS).toInstant(),
                faker.date().past(faker.random().nextInt(1, 15), TimeUnit.DAYS).toInstant());
    }

    private NoteSummaryDTO createNoteSummary() {
        return new NoteSummaryDTO(UUID.randomUUID(), faker.lorem().word(),
                faker.date().past(faker.random().nextInt(15, 30), TimeUnit.DAYS).toInstant(),
                faker.date().past(faker.random().nextInt(1, 15), TimeUnit.DAYS).toInstant());
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void findByTitleShouldReturnNoteDetailDTO() throws Exception {

        String title = exampleNoteDetail.getTitle();

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        when(noteService.findByTitleAndUser(title, exampleUser)).thenReturn(Optional.of(exampleNote));
        when(noteMapper.toDetailDTO(exampleNote)).thenReturn(exampleNoteDetail);

        mockMvc.perform(get("/notes/title/{title}", title))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exampleNoteDetail)));

        verify(userService).getAuthenticatedUser();
        verify(noteService).findByTitleAndUser(title, exampleUser);
        verify(noteMapper).toDetailDTO(exampleNote);
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void findByTitleShouldThrowNoteNotFoundWhenNoteDoesNotExist() throws Exception {

        String nonExistentNote = faker.lorem().word();

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        when(noteService.findByTitleAndUser(nonExistentNote, exampleUser)).thenReturn(Optional.empty());

        mockMvc.perform(get("/notes/title/{title}", nonExistentNote))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoteNotFoundException));

        verify(userService).getAuthenticatedUser();
        verify(noteService).findByTitleAndUser(nonExistentNote, exampleUser);
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void findAllShouldReturnNotesAsNoteSummaryDTOs() throws Exception {

        int pageNumber = 0;
        int pageSize = 10;
        Sort.Direction direction = Sort.Direction.DESC;
        String sortBy = "updatedAt";
        Sort sort = Sort.by(direction, sortBy);

        PageRequest request = PageRequest.of(pageNumber, pageSize, sort);
        Page<Note> page = new PageImpl<>(Collections.singletonList(exampleNote));

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        when(noteService.createPageRequestOf(pageNumber, pageSize, direction.name(), sortBy)).thenReturn(request);
        when(noteService.findUserNotes(request, exampleUser)).thenReturn(page);
        when(noteMapper.toSummaryDTO(page.toList())).thenReturn(exampleNoteSummaries);

        mockMvc.perform(get("/notes")
                .param("page", String.valueOf(pageNumber))
                .param("size", String.valueOf(pageSize))
                .param("direction", direction.name())
                .param("sort", sortBy))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exampleNoteSummaries)));

        verify(userService).getAuthenticatedUser();
        verify(noteService).createPageRequestOf(pageNumber, pageSize, direction.name(), sortBy);
        verify(noteService).findUserNotes(request, exampleUser);
        verify(noteMapper).toSummaryDTO(page.toList());
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void searchNotesShouldReturnNotesAsNoteSummaryDTOs() throws Exception {

        int pageNumber = 0;
        int pageSize = 10;
        Sort.Direction direction = Sort.Direction.DESC;
        String sortBy = "updatedAt";
        Sort sort = Sort.by(direction, sortBy);

        String term = "search term";
        boolean searchInContents = false;

        PageRequest request = PageRequest.of(pageNumber, pageSize, sort);
        Page<Note> page = new PageImpl<>(Collections.singletonList(exampleNote));

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        when(noteService.createPageRequestOf(pageNumber, pageSize, direction.name(), sortBy)).thenReturn(request);
        when(noteService.searchNotes(term, searchInContents, request, exampleUser)).thenReturn(page);
        when(noteMapper.toSummaryDTO(page.toList())).thenReturn(exampleNoteSummaries);

        mockMvc.perform(get("/notes/search")
                .param("term", term)
                .param("searchInContents", String.valueOf(searchInContents))
                .param("page", String.valueOf(pageNumber))
                .param("size", String.valueOf(pageSize))
                .param("direction", direction.name())
                .param("sort", sortBy))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exampleNoteSummaries)));

        verify(userService).getAuthenticatedUser();
        verify(noteService).createPageRequestOf(pageNumber, pageSize, term, sortBy);
        verify(noteService).searchNotes(term, searchInContents, request, exampleUser);
        verify(noteMapper).toSummaryDTO(page.toList());
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void createNoteShouldReturnLocationOfCreatedResource() throws Exception {

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        when(noteService.createNote(eq(exampleNoteDetail), eq(exampleUser), any())).thenReturn(exampleNote);

        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(exampleNoteDetail)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost/notes/title/" + exampleNoteDetail.getTitle()));

        verify(userService).getAuthenticatedUser();
        verify(noteService).createNote(any(NoteDetailDTO.class), eq(exampleUser), any());
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void createNoteShouldThrowInvalidNoteIfNoteFieldsHaveErrors() throws Exception {

        NoteDetailDTO badNoteDetailDTO = new NoteDetailDTO();

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        doThrow(new NoteValidationException(mock(Errors.class)))
                .when(noteService).createNote(eq(badNoteDetailDTO), eq(exampleUser), any());

        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badNoteDetailDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NoteValidationException));

        verify(userService).getAuthenticatedUser();
        verify(noteService).createNote(eq(badNoteDetailDTO), eq(exampleUser), any());
    }
}
