package com.example.nms.dto;

import java.util.UUID;

import com.example.nms.constants.MessageConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonIgnore
    UUID uuid;

    @NotBlank(message = MessageConstants.USERNAME_EMPTY)
    @Size(min = 2, max = 255, message = MessageConstants.USERNAME_LENGTH)
    String username;

    @NotBlank(message = MessageConstants.EMAIL_EMPTY)
    @Email(message = MessageConstants.EMAIL_NOT_VALID)
    String email;

    int numberOfNotes;

    public UserDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
