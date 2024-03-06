package com.example.nms.dto;

import java.time.Instant;
import java.util.UUID;

import com.example.nms.constants.MessageConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "uuid", "title" })
public class NoteSummaryDTO {

    @JsonIgnore
    UUID uuid;

    @NotBlank(message = MessageConstants.NOTE_TITLE_EMPTY)
    @Size(min = 1, max = 255, message = MessageConstants.NOTE_TITLE_LENGTH)
    String title;

    Instant createdAt;

    Instant updatedAt;
}
