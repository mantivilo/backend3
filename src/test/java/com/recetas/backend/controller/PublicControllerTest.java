package com.recetas.backend.controller;

import com.recetas.backend.model.Users;
import com.recetas.backend.service.RecipeService;
import com.recetas.backend.service.UserManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicControllerTest {

    private RecipeService recipeService;
    private UserManagementService userService;
    private PublicController publicController;

    @BeforeEach
    void setUp() {
        recipeService = mock(RecipeService.class);
        userService = mock(UserManagementService.class);
        publicController = new PublicController(recipeService, userService);
    }

    @Test
    void testGetHomePage() {
        // Arrange
        List<Map<String, Object>> recetasRecientes = List.of(
                Map.of("id", 1L, "nombre", "Receta1"),
                Map.of("id", 2L, "nombre", "Receta2")
        );
        List<Map<String, Object>> recetasPopulares = List.of(
                Map.of("id", 3L, "nombre", "Receta3"),
                Map.of("id", 4L, "nombre", "Receta4")
        );
        List<String> banners = List.of("Banner 1", "Banner 2");

        when(recipeService.getRecetasRecientes()).thenReturn(recetasRecientes);
        when(recipeService.getRecetasPopulares()).thenReturn(recetasPopulares);

        // Act
        Map<String, Object> response = publicController.getHomePage();

        // Assert
        assertNotNull(response);
        assertEquals(2, ((List<?>) response.get("recetasRecientes")).size());
        assertEquals(2, ((List<?>) response.get("recetasPopulares")).size());
        assertEquals(banners, response.get("banners"));
    }

    @Test
    void testBuscarRecetas() {
        // Arrange
        List<Map<String, Object>> recetas = List.of(
                Map.of("id", 1L, "nombre", "Receta1", "paisOrigen", "Chile"),
                Map.of("id", 2L, "nombre", "Receta2", "paisOrigen", "Mexico")
        );

        when(recipeService.buscarRecetas("Receta1", null, "Chile", null)).thenReturn(recetas);

        // Act
        List<Map<String, Object>> result = publicController.buscarRecetas("Receta1", null, "Chile", null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Receta1", result.get(0).get("nombre"));
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        Users user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        doNothing().when(userService).createUserAccount(user);

        // Act
        ResponseEntity<String> response = publicController.registerUser(user);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuario Ingresado.", response.getBody());
    }

    @Test
    void testRegisterUser_Failure() {
        // Arrange
        Users user = new Users();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");

        doThrow(new IllegalArgumentException("El nombre de usuario ya existe."))
                .when(userService).createUserAccount(user);

        // Act
        ResponseEntity<String> response = publicController.registerUser(user);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Error de registro: El nombre de usuario ya existe.", response.getBody());
    }
}
