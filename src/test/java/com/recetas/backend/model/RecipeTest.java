package com.recetas.backend.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void testDefaultConstructor() {
        // Arrange & Act
        Recipe recipe = new Recipe();

        // Assert
        assertNotNull(recipe);
        assertNull(recipe.getId());
        assertNull(recipe.getNombre());
        assertNull(recipe.getTipoCocina());
        assertNull(recipe.getPaisOrigen());
        assertNull(recipe.getDificultad());
        assertNull(recipe.getIngredientes());
        assertNull(recipe.getInstrucciones());
        assertNull(recipe.getFotografiaUrl());
        assertNull(recipe.getUrlVideo());
        assertEquals(0, recipe.getTiempoCoccion());
        assertNull(recipe.getComentariosValoraciones());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String nombre = "Tacos";
        String paisOrigen = "Mexico";
        String tipoCocina = "Mexicana";
        String dificultad = "Fácil";

        // Act
        Recipe recipe = new Recipe(nombre, paisOrigen, tipoCocina, dificultad);

        // Assert
        assertEquals(nombre, recipe.getNombre());
        assertEquals(tipoCocina, recipe.getTipoCocina());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Recipe recipe = new Recipe();
        Long id = 1L;
        String nombre = "Tacos";
        String tipoCocina = "Mexicana";
        String paisOrigen = "Mexico";
        String dificultad = "Media";
        String ingredientes = "Tortilla, carne, salsa";
        String instrucciones = "Preparar tacos";
        int tiempoCoccion = 30;
        String fotografiaUrl = "http://example.com/image.jpg";
        String urlVideo = "http://example.com/video.mp4";

        List<CommentValue> comentarios = new ArrayList<>();

        // Act
        recipe.setId(id);
        recipe.setNombre(nombre);
        recipe.setTipoCocina(tipoCocina);
        recipe.setPaisOrigen(paisOrigen);
        recipe.setDificultad(dificultad);
        recipe.setIngredientes(ingredientes);
        recipe.setInstrucciones(instrucciones);
        recipe.setTiempoCoccion(tiempoCoccion);
        recipe.setFotografiaUrl(fotografiaUrl);
        recipe.setUrlVideo(urlVideo);
        recipe.setComentariosValoraciones(comentarios);

        // Assert
        assertEquals(id, recipe.getId());
        assertEquals(nombre, recipe.getNombre());
        assertEquals(tipoCocina, recipe.getTipoCocina());
        assertEquals(paisOrigen, recipe.getPaisOrigen());
        assertEquals(dificultad, recipe.getDificultad());
        assertEquals(ingredientes, recipe.getIngredientes());
        assertEquals(instrucciones, recipe.getInstrucciones());
        assertEquals(tiempoCoccion, recipe.getTiempoCoccion());
        assertEquals(fotografiaUrl, recipe.getFotografiaUrl());
        assertEquals(urlVideo, recipe.getUrlVideo());
        assertEquals(comentarios, recipe.getComentariosValoraciones());
    }

    @Test
    void testComentariosValoraciones() {
        // Arrange
        Recipe recipe = new Recipe();
        List<CommentValue> comentarios = new ArrayList<>();
        CommentValue comment = new CommentValue();
        comentarios.add(comment);

        // Act
        recipe.setComentariosValoraciones(comentarios);

        // Assert
        assertNotNull(recipe.getComentariosValoraciones());
        assertEquals(1, recipe.getComentariosValoraciones().size());
        assertEquals(comment, recipe.getComentariosValoraciones().get(0));
    }
}
