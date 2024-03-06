package com.example.nms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.nms.dto.ErrorResponseDTO;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.exception.note.NoteValidationException;
import com.example.nms.exception.role.RoleIdNotFoundException;
import com.example.nms.exception.role.RoleNameNotFoundException;
import com.example.nms.exception.role.RoleValidationException;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.exception.user.UserNameNotFoundException;
import com.example.nms.exception.user.UserValidationException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class ApiGlobalExceptionHandler {

    @ExceptionHandler({ UsernameNotFoundException.class, UserNameNotFoundException.class, UserIdNotFoundException.class,
            NoteNotFoundException.class, RoleIdNotFoundException.class, RoleNameNotFoundException.class })
    public ResponseEntity<ErrorResponseDTO> notFoundHandler(Exception ex, HttpServletRequest request) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.create("Not found", ex, request, HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler({ BadCredentialsException.class, UserValidationException.class, NoteValidationException.class,
            RoleValidationException.class })
    public ResponseEntity<ErrorResponseDTO> badRequestHandler(Exception ex, HttpServletRequest request) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.create("Invalid data", ex, request, HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler({ LockedException.class, DisabledException.class })
    public ResponseEntity<ErrorResponseDTO> forbiddenHandler(Exception ex, HttpServletRequest request) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.create("Locked/Disabled user account", ex, request,
                HttpStatus.FORBIDDEN);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllUncaughtException(Exception ex, HttpServletRequest request) {

        ErrorResponseDTO errorResponse = ErrorResponseDTO.create("Unexpected error", ex, request,
                HttpStatus.INTERNAL_SERVER_ERROR);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
