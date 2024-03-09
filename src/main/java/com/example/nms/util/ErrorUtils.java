package com.example.nms.util;

import java.util.stream.Collectors;

import org.springframework.validation.Errors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtils {

    public static String formatErrors(Errors errors) {
        return errors.getFieldErrors().stream()
                .map(err -> err.getField() + " - " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
    }
}
