package com.example.nms.controller;

import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.service.auth.AuthService;
import com.example.nms.validator.UserDTOValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDTOValidator userDTOValidator;

    @PostMapping("/login")
    public AuthResponseDTO loginUser(@RequestBody @Valid LoginDTO loginDTO, BindingResult br) {

        if (br.hasErrors())
            throw new BadCredentialsException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        return authService.loginUser(loginDTO);
    }

    @PostMapping("/register")
    public AuthResponseDTO registerUser(@RequestBody @Valid RegisterDTO registerDTO, BindingResult br) {

        userDTOValidator.validate(registerDTO, br);
        if (br.hasErrors())
            throw new BadCredentialsException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        return authService.registerUser(registerDTO);
    }

}
