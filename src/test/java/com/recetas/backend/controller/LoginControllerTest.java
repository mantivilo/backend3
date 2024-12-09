package com.recetas.backend.controller;

import com.recetas.backend.security.JWTAuthtenticationConfig;
import com.recetas.backend.service.Implementation.IndUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private JWTAuthtenticationConfig jwtAuthtenticationConfig;
    private IndUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        jwtAuthtenticationConfig = mock(JWTAuthtenticationConfig.class);
        userDetailsService = mock(IndUserDetailsService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        loginController = new LoginController(jwtAuthtenticationConfig, userDetailsService, passwordEncoder);
    }

    @Test
    void testLogin_SuccessWithEncryptedPassword() {
        // Arrange
        String username = "testuser";
        String rawPassword = "password123";
        String encodedPassword = "$2a$10$abcdefg1234567890";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn(encodedPassword);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(jwtAuthtenticationConfig.getJWTToken(username)).thenReturn("mockToken");

        // Act
        ResponseEntity<String> response = loginController.login(username, rawPassword);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mockToken", response.getBody());
    }

    @Test
    void testLogin_SuccessWithUnencryptedPassword() {
        // Arrange
        String username = "testuser";
        String password = "password123";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn(password);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtAuthtenticationConfig.getJWTToken(username)).thenReturn("mockToken");

        // Act
        ResponseEntity<String> response = loginController.login(username, password);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mockToken", response.getBody());
    }

    @Test
    void testLogin_InvalidPasswordWithEncryption() {
        // Arrange
        String username = "testuser";
        String rawPassword = "wrongpassword";
        String encodedPassword = "$2a$10$abcdefg1234567890";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn(encodedPassword);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        // Act
        ResponseEntity<String> response = loginController.login(username, rawPassword);

        // Assert
        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Usuario y/o clave incorrecto", response.getBody());
    }

    @Test
    void testLogin_InvalidPasswordWithoutEncryption() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";
        String actualPassword = "password123";

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getPassword()).thenReturn(actualPassword);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // Act
        ResponseEntity<String> response = loginController.login(username, password);

        // Assert
        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Usuario y/o clave incorrecto", response.getBody());
    }
}
