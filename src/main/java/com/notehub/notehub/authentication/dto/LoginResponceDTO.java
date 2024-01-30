package com.notehub.notehub.authentication.dto;

import com.notehub.notehub.user.UserDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponceDTO {
    UserDTO userDTO;
    String jwtToken;
}
