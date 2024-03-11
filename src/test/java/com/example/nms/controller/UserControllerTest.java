package com.example.nms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nms.constants.RoleConstants;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.exception.user.UserValidationException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Faker faker;

    private User exampleUser;
    private UserDTO exampleUserDTO;

    @BeforeEach
    void setUp() {
        exampleUser = new User();
        exampleUser.setUuid(UUID.randomUUID());

        exampleUserDTO = createExampleUserDTO();
    }

    private UserDTO createExampleUserDTO() {

        return new UserDTO(UUID.randomUUID(), faker.name().username(), faker.internet().emailAddress(),
                faker.random().nextInt(30));
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void getUserProfileShouldReturnUserDTO() throws Exception {

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        when(userMapper.toDTO(exampleUser)).thenReturn(exampleUserDTO);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exampleUserDTO)));

        verify(userService).getAuthenticatedUser();
        verify(userMapper).toDTO(exampleUser);
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void updateUserShouldReturnUpdatedUserDTO() throws Exception {

        UUID userId = exampleUserDTO.getUuid();
        exampleUser.setUuid(userId);

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);
        when(userService.updateFromUserDTO(userId, exampleUserDTO)).thenReturn(exampleUser);
        when(userMapper.toDTO(exampleUser)).thenReturn(exampleUserDTO);

        String content = mapper.writeValueAsString(exampleUserDTO);

        mockMvc.perform(patch("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));

        verify(userService).getAuthenticatedUser();
        verify(userService).updateFromUserDTO(userId, exampleUserDTO);
        verify(userMapper).toDTO(exampleUser);
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void updateUserShouldThrowUserValidationIfUserDTOHasErrors() throws Exception {

        UserDTO badUserDTO = new UserDTO("", "bad email");

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);

        mockMvc.perform(patch("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserValidationException));

        verify(userService).getAuthenticatedUser();
        verify(userService, never()).updateFromUserDTO(exampleUser.getUuid(), badUserDTO);
    }

    @Test
    @WithMockUser(username = "user", roles = { RoleConstants.USER })
    void deleteUserShouldReturnNoContentIfUserWasSuccessfullyDeleted() throws Exception {

        when(userService.getAuthenticatedUser()).thenReturn(exampleUser);

        mockMvc.perform(delete("/user"))
                .andExpect(status().isNoContent());

        verify(userService).getAuthenticatedUser();
        verify(userService).deleteById(exampleUser.getUuid());
    }
}
