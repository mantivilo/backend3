package com.recetas.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentValueTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        CommentValue commentValue = new CommentValue();
        Long id = 1L;
        String comentario = "Delicious recipe!";
        Long valoracion = 5L;

        // Act
        commentValue.setId(id);
        commentValue.setComentario(comentario);
        commentValue.setValoracion(valoracion);

        // Assert
        assertEquals(id, commentValue.getId());
        assertEquals(comentario, commentValue.getComentario());
        assertEquals(valoracion, commentValue.getValoracion());
    }

    @Test
    void testRecipeAssociation() {
        // Arrange
        CommentValue commentValue = new CommentValue();
        Recipe recipe = new Recipe();

        // Act
        commentValue.setRecipe(recipe);

        // Assert
        assertNotNull(commentValue.getRecipe());
        assertEquals(recipe, commentValue.getRecipe());
    }
}
