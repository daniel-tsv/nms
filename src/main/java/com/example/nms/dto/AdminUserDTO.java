package com.example.nms.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import com.example.nms.entity.Role;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminUserDTO {

    UUID uuid;

    String username;

    String email;

    int numberOfNotes;

    Set<Role> roles;

    Instant createdAt;

    Boolean isAccountNonExpired;

    Boolean isAccountNonLocked;

    Boolean isCredentialsNonExpired;

    Boolean isEnabled;
}