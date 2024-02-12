package com.notehub.notehub.security.jwt;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.notehub.notehub.entity.User;
import com.notehub.notehub.exception.user.InvalidUserException;
import com.notehub.notehub.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private static final String USER_UUID = "UUID";

    @Value("${notehub.security.jwt.token-expiration-millis}")
    private long expiration;
    @Value("${notehub.security.jwt.token-secret}")
    private String secret;

    public String generateJWT(Authentication auth) {

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) auth.getPrincipal();
        User user = userDetailsImpl.getUser();

        if (user.getUuid() == null)
            throw new InvalidUserException("Failed to generate jwt token: User UUID is not set");

        return JWT.create()
                .withSubject("user-details")
                .withIssuer("self")
                .withIssuedAt(Instant.now())
                .withClaim(USER_UUID, user.getUuid().toString())
                .withExpiresAt(Instant.now().plusMillis(expiration))
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT validateAndDecodeToken(String token) throws JWTVerificationException {

        if (token == null || token.trim().isEmpty())
            throw new JWTVerificationException("JWT Token cannot be empty");

        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .withSubject("user-details")
                .withClaimPresence(USER_UUID)
                .withIssuer("self")
                .build();

        DecodedJWT jwt = verifier.verify(token);

        Claim userUuidClaim = jwt.getClaim(USER_UUID);
        if (userUuidClaim == null || userUuidClaim.asString().isEmpty())
            throw new JWTVerificationException("Missing or empty USER_UUID in JWT Token");

        return jwt;
    }

    public String extractUserId(DecodedJWT jwt) {
        return jwt.getClaim(USER_UUID).asString();
    }
}
