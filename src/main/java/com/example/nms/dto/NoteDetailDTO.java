package com.example.nms.dto;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(of = { "uuid", "title" })
public class NoteDetailDTO {

    @JsonIgnore
    UUID uuid;

    @NotBlank(message = "Title cannot be empty or consist only of whitespace characters")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    String title;

    String contents;

    Instant createdAt;

    Instant updatedAt;
}
