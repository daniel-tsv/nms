package com.notehub.notehub.user;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserDTO {

    @JsonIgnore
    UUID uuid;

    @NotBlank(message = "Username cannot be empty or consist only of whitespace characters")
    @Size(min = 2, max = 255, message = "Username length must be between 2 and 255 characters")
    String username;

    @NotBlank(message = "Password cannot be empty or consist only of whitespace characters")
    String password;

    @NotBlank(message = "Email cannot be empty or consist only of whitespace characters")
    @Email(message = "Email should be valid")
    String email;

    public UserDTO(UUID uuid, String username, String password, String email) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.email = email.toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
