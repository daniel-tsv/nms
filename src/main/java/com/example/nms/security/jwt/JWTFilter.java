package com.example.nms.security.jwt;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.nms.dto.ErrorResponseDTO;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final Logger myLogger;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
                logRequestDetails(request);
                filterChain.doFilter(request, response);
                return;
            }

            DecodedJWT decodedJwt = jwtProvider.validateAndDecodeToken(authHeader.substring(7));

            UUID uuid = UUID.fromString(jwtProvider.extractUserId(decodedJwt));
            UserDetails userDetails = userDetailsServiceImpl.loadUserById(uuid);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                    userDetails.getPassword(), userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (UserIdNotFoundException ex) {
            handleException(request, response, ex, HttpStatus.NOT_FOUND);
        } catch (JWTVerificationException ex) {
            handleException(request, response, ex, HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            handleException(request, response, ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // Global exception handler wont catch exceptions thrown at filter level, so we have to do it here
    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception ex,
            HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        ErrorResponseDTO errorResponse = ErrorResponseDTO.create("Error", ex, request, status);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private void logRequestDetails(HttpServletRequest request) {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append("; ");
        }

        StringBuilder parameters = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            parameters.append(paramName).append("=").append(paramValue).append("&");
        }

        myLogger.info(
                "Got request without auth header: method={}, requestURI={}, queryString={}, headers={}, parameters={}",
                method, requestURI, queryString, headers, parameters);
    }
}
