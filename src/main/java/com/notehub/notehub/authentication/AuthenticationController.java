package com.notehub.notehub.authentication;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.notehub.notehub.authentication.dto.LoginDTO;
import com.notehub.notehub.authentication.dto.RegisterDTO;
import com.notehub.notehub.authentication.dto.ResponceDTO;
import com.notehub.notehub.util.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponceDTO loginUser(@RequestBody @Validated LoginDTO loginDTO, BindingResult br) {

        if (br.hasErrors())
            throw new InvalidCredentialsException("Invalid user credentials");

        return authenticationService.loginUser(loginDTO);
    }

    @PostMapping("/register")
    public ResponceDTO registerUser(@RequestBody @Validated RegisterDTO registerDTO, BindingResult br) {

        if (br.hasErrors())
            throw new InvalidCredentialsException("Invalid user credentials");

        return authenticationService.registerUser(registerDTO);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse wrongCredentialsHandler(InvalidCredentialsException ex, HttpServletRequest request) {
        return new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST, "Invalid credentials",
                "Entered credentials are invalid", request.getRequestURI());
    }
}
