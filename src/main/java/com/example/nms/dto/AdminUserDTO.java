package com.example.nms.dto;

import java.util.Set;
import java.util.UUID;

import com.example.nms.entity.Role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AdminUserDTO {

    UUID uuid;

    String username;

    int numberOfNotes;

    Set<Role> roles;

    public AdminUserDTO(String username) {
        this.username = username;
    }
}