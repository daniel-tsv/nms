package com.example.nms.security;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImpl(userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "The user with username '" + username + "' does not exist")));
    }

    public UserDetails loadUserById(UUID uuid) throws UserIdNotFoundException {
        return new UserDetailsImpl(userService.findById(uuid)
                .orElseThrow(() -> new UserIdNotFoundException("The user with id '" + uuid + "' does not exist")));
    }
}
