package com.notehub.notehub.user.controllers;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.notehub.notehub.user.User;
import com.notehub.notehub.user.UserDTO;
import com.notehub.notehub.user.UserDTOValidator;
import com.notehub.notehub.user.UserErrorResponse;
import com.notehub.notehub.user.UserMapper;
import com.notehub.notehub.user.UserService;
import com.notehub.notehub.user.exceptions.EmailAlreadyExistsException;
import com.notehub.notehub.user.exceptions.InvalidUserException;
import com.notehub.notehub.user.exceptions.UserNotFoundException;
import com.notehub.notehub.user.exceptions.UsernameAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final UserDTOValidator userDTOValidator;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findByUUID(@PathVariable(name = "id") UUID id) {
        return ResponseEntity.ok(userMapper.convertToDTO(userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("The user with this id does not exist"))));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
            @RequestParam(name = "sort", required = false, defaultValue = "uuid") String sortBy) {

        List<User> users = userService.listUsers(page, size, direction, sortBy).getContent();

        return ResponseEntity.ok(userMapper.convertToDTO(users));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Validated UserDTO userDTO,
            BindingResult br) {
        userDTOValidator.validate(userDTO, br);

        if (br.hasErrors())
            throw new InvalidUserException(
                    "user is not valid: " + br.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; ")));

        User user = userMapper.convertToEntity(userDTO);
        User createdUser = userService.createUser(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdUser.getUuid()).toUri();

        return ResponseEntity.created(location).body(userMapper.convertToDTO(createdUser));
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") UUID id, @RequestBody @Validated UserDTO userDTO,
            BindingResult br) {
        userDTOValidator.validate(userDTO, br);

        if (br.hasErrors())
            throw new InvalidUserException(
                    "user is not valid: " + br.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                            .collect(Collectors.joining("; ")));

        User user = userMapper.convertToEntity(userDTO);
        UserDTO updatedUserDTO = userMapper.convertToDTO(userService.updateUser(id, user));

        return ResponseEntity.ok(updatedUserDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(@PathVariable("id") UUID id) {
        userService.deleteUser(id);
        return "Successfully deleted user with id: {id}";
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorResponse userNotFoundHandler(UserNotFoundException ex, HttpServletRequest request) {
        return new UserErrorResponse(Instant.now(), HttpStatus.NOT_FOUND, "User not found", ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public UserErrorResponse usernameAlreadyExists(UsernameAlreadyExistsException ex, HttpServletRequest request) {
        return new UserErrorResponse(Instant.now(), HttpStatus.CONFLICT, "This username is already taken",
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public UserErrorResponse emailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return new UserErrorResponse(Instant.now(), HttpStatus.CONFLICT, "This email is already taken",
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorResponse invalidUserException(InvalidUserException ex, HttpServletRequest request) {
        return new UserErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST, "Invalid user data",
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorResponse illegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return new UserErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST, "Passed arguments are invalid",
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public UserErrorResponse methodArgumentTypeMismatchHandler(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        return new UserErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST, "Invalid Argument",
                "The provided UUID is not valid.", request.getRequestURI());
    }
}
