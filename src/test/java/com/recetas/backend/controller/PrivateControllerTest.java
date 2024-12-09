package com.recetas.backend.controller;

import com.recetas.backend.model.CommentValue;
import com.recetas.backend.model.CommentValueView;
import com.recetas.backend.model.Recipe;
import com.recetas.backend.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrivateControllerTest {

    private RecipeService recipeService;
    private PrivateController privateController;

    @BeforeEach
    void setUp() {
        recipeService = mock(RecipeService.class);
        privateController = new PrivateController(recipeService);
    }

    @Test
    void testListarRecetas_EmptyResult() {
        // Arrange
        when(recipeService.listarRecetas()).thenReturn(Collections.emptyList());

        // Act
        List<Recipe> result = privateController.listarRecetas();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetRecetaDetails_NotFound() {
        // Arrange
        when(recipeService.getRecetaById(1L)).thenReturn(null);

        // Act
        Recipe result = privateController.getRecetaDetails(1L);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetDetalleReceta_EmptyResult() {
        // Arrange
        when(recipeService.detalleReceta(1L)).thenReturn(Collections.emptyMap());

        // Act
        Map<String, Object> result = privateController.getDetalleReceta(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGuardarComentarioValoracion_NullComment() {
        // Act
        ResponseEntity<CommentValueView> response = privateController.guardarComentarioValoracion(1L, null);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testGuardarComentarioValoracion_InvalidValoracion() {
        // Arrange
        CommentValue comment = new CommentValue();
        comment.setComentario("Good");
        comment.setValoracion(6L); // Invalid valoracion

        // Act
        ResponseEntity<CommentValueView> response = privateController.guardarComentarioValoracion(1L, comment);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testGuardarComentarioValoracion_ServiceError() {
        // Arrange
        CommentValue comment = new CommentValue();
        comment.setComentario("Good");
        comment.setValoracion(4L);

        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.getRecetaById(1L)).thenReturn(recipe);
        doThrow(new RuntimeException("Service error")).when(recipeService).guardarComentarioValoracion(comment);

        // Act
        ResponseEntity<CommentValueView> response = privateController.guardarComentarioValoracion(1L, comment);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testAgregarVideo_EmptyUrl() {
        // Act
        ResponseEntity<String> response = privateController.agregarVideo(1L, "");

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue()); // Expecting 400 BAD_REQUEST
        assertEquals("Error de ingreso: URL del video no puede estar vacía.", response.getBody());
    }


    @Test
    void testCrearReceta_NullBody() {
        // Act
        ResponseEntity<String> response = privateController.crearReceta(null);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("No se recibió el cuerpo de la receta.", response.getBody());
    }

    @Test
    void testListarComentariosValoracion_EmptyResult() {
        // Arrange
        when(recipeService.getComentarioValoracionByRecetaId(1L)).thenReturn(Collections.emptyList());

        // Act
        List<CommentValueView> result = privateController.listarCometariosValoracion(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
