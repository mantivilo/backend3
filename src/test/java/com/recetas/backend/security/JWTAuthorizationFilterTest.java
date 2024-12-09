package com.recetas.backend.security;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;

import java.util.Collections;

import static com.recetas.backend.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JWTAuthorizationFilterTest {

    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {
        jwtAuthorizationFilter = new JWTAuthorizationFilter();
    }

    @Test
    void testValidJWT() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String validToken = Jwts.builder()
                .setSubject("user")
                .claim("authorities", Collections.singletonList("ROLE_USER"))
                .signWith((SecretKey) getSigningKey(KEY))
                .compact();

        request.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + validToken);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("user", SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testInvalidJWT() throws Exception {
        // Clear the SecurityContextHolder before the test
        SecurityContextHolder.clearContext();

        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String invalidToken = "invalid_token";
        request.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + invalidToken);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication()); // Authentication should remain null
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus()); // Response should be 403 Forbidden
        verify(filterChain, times(0)).doFilter(request, response);

        // Clear the SecurityContextHolder after the test
        SecurityContextHolder.clearContext();
    }


    @Test
    void testMissingJWT() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response); // Proceed without authentication
    }

    @Test
    void testExpiredJWT() throws Exception {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        String expiredToken = Jwts.builder()
                .setSubject("user")
                .claim("authorities", Collections.singletonList("ROLE_USER"))
                .signWith((SecretKey) getSigningKey(KEY))
                .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000)) // Expired time
                .compact();

        request.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + expiredToken);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
        verify(filterChain, times(0)).doFilter(request, response);
    }
}
