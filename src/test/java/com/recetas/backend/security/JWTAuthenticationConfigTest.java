package com.recetas.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.recetas.backend.constants.Constants.KEY;
import static org.junit.jupiter.api.Assertions.*;

class JWTAuthenticationConfigTest {

    private JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @BeforeEach
    void setUp() {
        jwtAuthtenticationConfig = new JWTAuthtenticationConfig();
    }

    @Test
    void testGetJWTToken() {
        // Arrange
        String username = "testUser";

        // Act
        String token = jwtAuthtenticationConfig.getJWTToken(username);

        // Assert
        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));

        // Remove the "Bearer " prefix to parse the JWT
        String jwt = token.replace("Bearer ", "");

        // Parse the JWT and verify its contents
        Key key = Keys.hmacShaKeyFor(KEY.getBytes(StandardCharsets.UTF_8));
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        assertEquals(username, claims.getSubject());
        assertNotNull(claims.get("authorities"));
        assertTrue(claims.get("authorities").toString().contains("ROLE_USER"));
        assertTrue(claims.getExpiration().after(new Date())); // Verify expiration time
    }
}
