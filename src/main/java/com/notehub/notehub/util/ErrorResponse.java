package com.notehub.notehub.util;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private Instant timestamp;
    private HttpStatus status;
    private String error;
    private String message;
    private String path;
}
