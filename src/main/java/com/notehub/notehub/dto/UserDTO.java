package com.notehub.notehub.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    String username;

    String email;

    int numberOfNotes;

    public UserDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
