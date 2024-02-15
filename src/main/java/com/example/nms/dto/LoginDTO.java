package com.example.nms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDTO {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 255, message = "Username length must be between 2 and 255 characters")
    String username;

    @NotBlank(message = "Password cannot be empty")
    String password;
}