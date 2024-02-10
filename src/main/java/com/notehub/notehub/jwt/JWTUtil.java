package com.notehub.notehub.jwt;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private static final String USERNAME_CLAIM = "username";

    @Value("${notehub.security.jwt.token-expiration-millis}")
    private long expiration;
    @Value("${notehub.security.jwt.token-secret}")
    private String secret;

    public String generateJWT(Authentication auth) {

        return JWT.create()
                .withSubject("user-details")
                .withIssuer("self")
                .withIssuedAt(Instant.now())
                .withClaim(USERNAME_CLAIM, auth.getName())
                .withExpiresAt(Instant.now().plusMillis(expiration))
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT validateAndDecodeToken(String token) throws JWTVerificationException {

        if (token.isBlank())
            throw new JWTVerificationException("JWT Token cannot be blank");

        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .withSubject("user-details")
                .withIssuer("self")
                .withClaimPresence(USERNAME_CLAIM)
                .build();

        return verifier.verify(token);
    }

    public String extractUsername(DecodedJWT jwt) {
        return jwt.getClaim(USERNAME_CLAIM).asString();
    }
}
