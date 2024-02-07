package com.notehub.notehub.controllers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.notehub.notehub.dto.AuthResponseDTO;
import com.notehub.notehub.dto.LoginDTO;
import com.notehub.notehub.dto.RegisterDTO;
import com.notehub.notehub.services.AuthenticationService;
import com.notehub.notehub.util.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthResponseDTO loginUser(@RequestBody LoginDTO loginDTO) {
        return authenticationService.loginUser(loginDTO);
    }

    @PostMapping("/register")
    public AuthResponseDTO registerUser(@RequestBody RegisterDTO registerDTO) {
        return authenticationService.registerUser(registerDTO);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse usernameNotFoundHandler(UsernameNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND, "Username not found",
                ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badCredentialsHandler(BadCredentialsException ex, HttpServletRequest request) {
        return new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST, "Bad credentials",
                ex.getMessage(), request.getRequestURI());
    }
}
