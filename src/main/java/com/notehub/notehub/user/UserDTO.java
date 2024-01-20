package com.notehub.notehub.user;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonIgnore
    private UUID uuid;

    @NotBlank(message = "Username cannot be empty or consist only of whitespace characters")
    @Size(min = 2, max = 255, message = "Username length must be between 2 and 255 characters")
    private String username;

    @NotBlank(message = "Password cannot be empty or consist only of whitespace characters")
    private String password;

    @NotBlank(message = "Email cannot be empty or consist only of whitespace characters")
    @Email(message = "Email should be valid")
    private String email;
}
