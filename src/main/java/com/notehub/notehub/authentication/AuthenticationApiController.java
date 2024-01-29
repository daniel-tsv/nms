package com.notehub.notehub.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.notehub.notehub.authentication.dto.LoginDTO;
import com.notehub.notehub.authentication.dto.RegisterDTO;
import com.notehub.notehub.user.UserDTO;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*") // TODO remove
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationApiController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody @Validated LoginDTO loginDTO, BindingResult br) {

        if (br.hasErrors())
            throw new WrongCredentialsException("Invalid user credentials");

        return authenticationService.loginUser(loginDTO);
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody @Validated RegisterDTO registerDTO, BindingResult br) {

        if (br.hasErrors())
            throw new WrongCredentialsException("Invalid user credentials");

        return authenticationService.registerUser(registerDTO);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String wrongCredentialsHandler(WrongCredentialsException ex) {
        return ex.getMessage();
        // TODO return correct responce
    }
}
