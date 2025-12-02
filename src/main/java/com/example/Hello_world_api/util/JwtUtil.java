package com.example.Hello_world_api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // Secret key for signing the JWT (should be kept secure)
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token expiration time: 30 minutes
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;

    /**
     * Generate a JWT token with user ID and role.
     *
     * @param userId User ID to include in token
     * @param role   Role of the user
     * @return JWT token string
     */
    public static String generateToken(String userId, String role) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
}

    //Validate a J*WT token and return claims if valid.