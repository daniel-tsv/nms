package com.example.nms.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConstants {

    public static final String USER_ID_NOT_FOUND = "User with id %s was not found";
    public static final String USER_USERNAME_NOT_FOUND = "User with username %s was not found";
    public static final String USER_NOT_VALID = "User is not valid: ";
    public static final String USER_DELETED = "User was deleted successfully";

    public static final String USERNAME_ALREADY_TAKEN = "Username %s is already taken";
    public static final String EMAIL_ALREADY_TAKEN = "Email %s is already taken";

    public static final String NOTE_TITLE_EXISTS = "Note with title %s already exists";
    public static final String NOTE_NOT_FOUND = "Note %s was not found";
    public static final String NOTE_DELETED = "Note %s was deleted successfully";

    public static final String AUTH_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String AUTH_ACCOUNT_LOCKED = "Your account has been locked";
    public static final String AUTH_ACCOUNT_DISABLED = "Your account has been disabled";

    public static final String ROLE_NOT_FOUND = "Role %s was not found";

}
