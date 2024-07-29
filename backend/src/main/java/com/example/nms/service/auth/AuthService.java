package com.example.nms.service.auth;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;

public interface AuthService {

    AuthResponseDTO loginUser(LoginDTO loginDTO);

    AuthResponseDTO registerUser(RegisterDTO registerDTO);
}
