package com.recetas.backend.service;

import com.recetas.backend.model.Users;
import com.recetas.backend.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserManagementServiceTest {

    private IUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserManagementService userManagementService;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userManagementService = new UserManagementService(userRepository, passwordEncoder);
    }

    @Test
    void testCreateUserAccount_Success() {
        // Arrange
        Users user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Act
        userManagementService.createUserAccount(user);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testCreateUserAccount_UsernameAlreadyExists() {
        // Arrange
        Users user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userManagementService.createUserAccount(user);
        });
        assertEquals("El nombre de usuario ya existe.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUserAccount_EmailAlreadyExists() {
        // Arrange
        Users user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userManagementService.createUserAccount(user);
        });
        assertEquals("El correo electrónico ya está registrado.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
