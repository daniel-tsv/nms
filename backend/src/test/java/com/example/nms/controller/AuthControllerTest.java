package com.example.nms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.exception.auth.AuthValidationException;
import com.example.nms.service.auth.AuthService;
import com.example.nms.validator.UserDTOValidator;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.datafaker.Faker;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private UserDTOValidator userDTOValidator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Faker faker;

    private String username;
    private String password;
    private String email;
    private String token;

    @BeforeEach
    void setUp() {
        username = faker.internet().username();
        password = faker.internet().password();
        email = faker.internet().emailAddress();
        token = faker.bothify("?#???????#.?#???????#?.??????#?????#");
    }

    @Test
    void loginUserShouldReturnAuthResponseDTO() throws Exception {

        LoginDTO loginDTO = new LoginDTO(username, password);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(new UserDTO(username, password), token);

        when(authService.loginUser(loginDTO)).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authResponseDTO)));

        verify(authService).loginUser(loginDTO);
    }

    @Test
    void loginUserShouldThrowAuthValidationWhenLoginDTOHasErrors() throws Exception {

        LoginDTO badLoginDTO = new LoginDTO("s", "");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badLoginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthValidationException));
    }

    @Test
    void registerUserShouldReturnAuthResponseDTO() throws Exception {

        RegisterDTO registerDTO = new RegisterDTO(username, password, email);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(new UserDTO(username, email), token);

        when(authService.registerUser(registerDTO)).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authResponseDTO)));

        verify(authService).registerUser(registerDTO);
    }

    @Test
    void registerUserShouldThrowAuthValidationWhenRegisterDTOHasErrors() throws Exception {

        RegisterDTO badRegisterDTO = new RegisterDTO("u", "", "email");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badRegisterDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AuthValidationException));
    }
}
