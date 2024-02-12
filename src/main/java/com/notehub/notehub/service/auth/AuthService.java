package com.notehub.notehub.service.auth;

import com.notehub.notehub.dto.AuthResponseDTO;
import com.notehub.notehub.dto.LoginDTO;
import com.notehub.notehub.dto.RegisterDTO;
import com.notehub.notehub.entity.User;

public interface AuthService {

    AuthResponseDTO loginUser(LoginDTO loginDTO);

    AuthResponseDTO registerUser(RegisterDTO registerDTO);

    User getAuthenticatedUser();

}
