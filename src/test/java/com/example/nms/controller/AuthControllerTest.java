package com.example.nms.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.dto.UserDTO;
import com.example.nms.service.auth.AuthService;
import com.example.nms.validator.UserDTOValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

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
        username = faker.name().username();
        password = faker.internet().password();
        email = faker.internet().emailAddress();
        token = faker.bothify("?#???????#.?#???????#?.??????#?????#");
    }

    @Test
    void loginUserShouldReturnAuthResponseDTO() throws Exception {

        LoginDTO loginDTO = new LoginDTO(username, password);
        UserDTO userDTO = new UserDTO(username, password);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(userDTO, token);

        when(authService.loginUser(eq(loginDTO), any())).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authResponseDTO)));

        verify(authService).loginUser(eq(loginDTO), any());
    }

    @Test
    void loginUserShouldThrowBadCredentialsWhenLoginDTOFieldsHaveErrors() throws Exception {

        LoginDTO badLoginDTO = new LoginDTO("s", "");

        doThrow(new BadCredentialsException("field errors"))
                .when(authService).loginUser(eq(badLoginDTO), any());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badLoginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof BadCredentialsException));

        verify(authService).loginUser(eq(badLoginDTO), any());
    }

    @Test
    void registerUserShouldReturnAuthResponseDTO() throws Exception {

        RegisterDTO registerDTO = new RegisterDTO(username, password, email);
        UserDTO userDTO = new UserDTO(username, email);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(userDTO, token);

        when(authService.registerUser(eq(registerDTO), any())).thenReturn(authResponseDTO);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authResponseDTO)));

        verify(authService).registerUser(eq(registerDTO), any());
    }

    @Test
    void registerUserShouldThrowBadCredentialsWhenRegisterDTOFieldsHaveErrors() throws Exception {

        RegisterDTO badRegisterDTO = new RegisterDTO("u", "", "email");

        doThrow(new BadCredentialsException("field errors"))
                .when(authService).registerUser(eq(badRegisterDTO), any());

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(badRegisterDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadCredentialsException));

        verify(authService).registerUser(eq(badRegisterDTO), any());
    }
}
