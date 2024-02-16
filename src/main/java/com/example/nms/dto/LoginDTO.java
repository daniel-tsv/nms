package com.example.nms.dto;

import com.example.nms.constants.MessageConstants;

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

    @NotBlank(message = MessageConstants.USERNAME_EMPTY)
    @Size(min = 2, max = 255, message = MessageConstants.USERNAME_LENGTH)
    String username;

    @NotBlank(message = MessageConstants.PASSWORD_EMPTY)
    String password;
}
