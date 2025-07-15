package org.banking.service.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.Date;

/**
 * Utility class for generating and validating JWT tokens.
 */
public class JwtUtil {
    private static final String SECRET_KEY = "my-very-secret-key";
    private static final long EXPIRATION_MS = 3600_000; // 1 hour

    /**
     * Generates a JWT token for the given subject (userId).
     * @param subject the subject (userId) for the token
     * @return the generated JWT token
     */
    public static String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Validates a JWT token and returns the subject (userId).
     * @param token the JWT token
     * @return the subject (userId) if valid
     * @throws IllegalArgumentException if the token is invalid or expired
     */
    public static String validateTokenAndGetSubject(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
    }
} 