package com.example.nms.security.jwt;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.nms.constants.MessageConstants;
import com.example.nms.security.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final Logger myLogger;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            logRequestDetails(request);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            DecodedJWT decodedJwt = jwtProvider.validateAndDecodeToken(authHeader.substring(7));

            UUID uuid = UUID.fromString(jwtProvider.extractUserId(decodedJwt));
            UserDetails userDetails = userDetailsServiceImpl.loadUserById(uuid);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                    userDetails.getPassword(), userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (JWTVerificationException e) {
            // todo return proper response
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    String.format(MessageConstants.TOKEN_INVALID, e.getMessage()));
        }

        filterChain.doFilter(request, response);
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
