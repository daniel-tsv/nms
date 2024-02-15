package com.example.nms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.nms.dto.ErrorResponseDTO;
import com.example.nms.exception.note.InvalidNoteException;
import com.example.nms.exception.note.NoteNotFoundException;
import com.example.nms.exception.user.InvalidUserException;
import com.example.nms.exception.user.UserIdNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class ApiGlobalExceptionHandler {

    @ExceptionHandler({ UsernameNotFoundException.class, UserIdNotFoundException.class, NoteNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO notFoundHandler(Exception ex, HttpServletRequest request) {
        return ErrorResponseDTO.create("Not found", ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BadCredentialsException.class, InvalidUserException.class, InvalidNoteException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO badRequestHandler(Exception ex, HttpServletRequest request) {
        return ErrorResponseDTO.create("Invalid data", ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ LockedException.class, DisabledException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO forbiddenHandler(Exception ex, HttpServletRequest request) {
        return ErrorResponseDTO.create("Locked/Disabled user account", ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponseDTO handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        return ErrorResponseDTO.create("Unexpected error", ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
