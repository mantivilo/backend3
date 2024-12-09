package com.recetas.backend.service.implementation;

import com.recetas.backend.model.Users;
import com.recetas.backend.repository.IUserRepository;
import com.recetas.backend.service.Implementation.IndUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IndUserDetailsServiceTest {

    private IUserRepository userRepository;
    private IndUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        userDetailsService = new IndUserDetailsService(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        // Arrange
        String username = "testuser";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword("password123");
        when(userRepository.findOneByUsername(username)).thenReturn(user);

        // Act
        var result = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findOneByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
        assertEquals(username, exception.getMessage());
    }
}
