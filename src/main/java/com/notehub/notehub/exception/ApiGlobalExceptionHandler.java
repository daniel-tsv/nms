package com.notehub.notehub.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.notehub.notehub.exception.user.InvalidUserException;
import com.notehub.notehub.exception.user.UserIdNotFoundException;
import com.notehub.notehub.util.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApiGlobalExceptionHandler {

    @ExceptionHandler({ UsernameNotFoundException.class, UserIdNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(Exception ex, HttpServletRequest request) {
        return createErrorResponse("Not found", ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BadCredentialsException.class, InvalidUserException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badCredentialsHandler(BadCredentialsException ex, HttpServletRequest request) {
        return createErrorResponse("Invalid data", ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ LockedException.class, DisabledException.class })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse lockedHandler(LockedException ex, HttpServletRequest request) {
        return createErrorResponse("Locked/Disabled user account", ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        return createErrorResponse("Unexpected error", ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse createErrorResponse(String error, Exception ex, HttpServletRequest request,
            HttpStatus status) {
        return new ErrorResponse(error, ex.getMessage(), request.getRequestURI(), status, Instant.now());
    }
}
