package com.example.nms.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponseDTO {

    String error;

    String message;

    String path;

    HttpStatus status;

    Instant timestamp;

    public static ErrorResponseDTO create(String error, Exception ex, HttpServletRequest request,
            HttpStatus status) {
        return new ErrorResponseDTO(error, ex.getMessage(), request.getRequestURI(), status, Instant.now());
    }
}
