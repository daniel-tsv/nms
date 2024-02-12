package com.example.nms.service.auth;

import com.example.nms.dto.AuthResponseDTO;
import com.example.nms.dto.LoginDTO;
import com.example.nms.dto.RegisterDTO;
import com.example.nms.entity.User;

public interface AuthService {

    AuthResponseDTO loginUser(LoginDTO loginDTO);

    AuthResponseDTO registerUser(RegisterDTO registerDTO);

    User getAuthenticatedUser();

}
