package com.recetas.backend.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    @Test
    //prueba getters and setters
    void testGettersAndSetters() {
        // Arrange
        Users user = new Users();
        Integer id = 1;
        String username = "testUser";
        String name = "Test Name";
        String email = "test@example.com";
        String password = "password123";

        // Act
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    //prueba GetAuthorities
    void testGetAuthorities() {
        // Arrange
        Users user = new Users();

        // Act
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    //prueba los metodos no implementados
    void testUnimplementedMethods() {
        // Arrange
        Users user = new Users();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, user::isAccountNonExpired);
        assertThrows(UnsupportedOperationException.class, user::isAccountNonLocked);
        assertThrows(UnsupportedOperationException.class, user::isCredentialsNonExpired);
        assertThrows(UnsupportedOperationException.class, user::isEnabled);
    }
}
