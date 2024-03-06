package com.example.nms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.service.auth.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@Valid @RequestBody LoginDTO loginDTO, BindingResult br) {

        return ResponseEntity.ok(authService.loginUser(loginDTO, br));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody @Valid RegisterDTO registerDTO, BindingResult br) {

        return ResponseEntity.ok(authService.registerUser(registerDTO, br));
    }

}
