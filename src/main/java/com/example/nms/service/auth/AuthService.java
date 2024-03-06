package com.example.nms.service.auth;

import org.springframework.validation.Errors;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;

public interface AuthService {

    AuthResponseDTO loginUser(LoginDTO loginDTO, Errors errors);

    AuthResponseDTO registerUser(RegisterDTO registerDTO, Errors errors);

}
