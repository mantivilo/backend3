package com.recetas.backend.service;

import com.recetas.backend.model.CommentValue;
import com.recetas.backend.model.CommentValueView;
import com.recetas.backend.model.Recipe;
import com.recetas.backend.repository.CommentValueRepository;
import com.recetas.backend.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    private RecetaRepository recetaRepository;
    private CommentValueRepository commentValueRepository;
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recetaRepository = mock(RecetaRepository.class);
        commentValueRepository = mock(CommentValueRepository.class);
        recipeService = new RecipeService(recetaRepository, commentValueRepository);
    }

    @Test
    void testListarRecetas() {
        // Arrange
        List<Recipe> recetas = List.of(new Recipe(), new Recipe());
        when(recetaRepository.findAll()).thenReturn(recetas);

        // Act
        List<Recipe> result = recipeService.listarRecetas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetRecetasRecientes() {
        // Arrange
        List<Object[]> recetaDataList = new ArrayList<>();
        recetaDataList.add(new Object[]{1L, "Receta1", "Chile", "Latina", "Media"});
        when(recetaRepository.findRecetasRecientes()).thenReturn(recetaDataList);

        // Act
        List<Map<String, Object>> result = recipeService.getRecetasRecientes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Receta1", result.get(0).get("nombre"));
    }

    @Test
    void testGetRecetasPopulares() {
        // Arrange
        List<Object[]> recetaDataList = new ArrayList<>();
        recetaDataList.add(new Object[]{1L, "Receta1", "Chile", "Latina", "Media"});
        when(recetaRepository.findRecetasPopulares()).thenReturn(recetaDataList);

        // Act
        List<Map<String, Object>> result = recipeService.getRecetasPopulares();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Receta1", result.get(0).get("nombre"));
    }

    @Test
    void testBuscarRecetas() {
        // Arrange
        List<Object[]> recetaDataList = new ArrayList<>();
        recetaDataList.add(new Object[]{1L, "Receta1", "Chile", "Latina", "Media"});
        when(recetaRepository.findRecetasByFields("Receta1", "Latina", "Chile", "Media"))
                .thenReturn(recetaDataList);

        // Act
        List<Map<String, Object>> result = recipeService.buscarRecetas("Receta1", "Latina", "Chile", "Media");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Receta1", result.get(0).get("nombre"));
    }

    @Test
    void testDetalleReceta_Success() {
        // Arrange
        Recipe receta = new Recipe();
        receta.setId(1L);
        receta.setDificultad("Media");
        receta.setIngredientes("Ingrediente1, Ingrediente2");
        receta.setInstrucciones("Paso1, Paso2");
        receta.setTiempoCoccion(60);
        receta.setFotografiaUrl("http://example.com/image.jpg");
        receta.setUrlVideo("http://example.com/video.mp4");
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        // Act
        Map<String, Object> result = recipeService.detalleReceta(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get("id"));
        assertEquals("Media", result.get("dificultad"));
    }

    @Test
    void testDetalleReceta_NotFound() {
        // Arrange
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recipeService.detalleReceta(1L);
        });
        assertEquals("busqueda sin exito", exception.getMessage());
    }

    @Test
    void testListarComentarioValoracion() {
        // Arrange
        List<CommentValue> comments = List.of(new CommentValue(), new CommentValue());
        when(commentValueRepository.findAll()).thenReturn(comments);

        // Act
        List<CommentValue> result = recipeService.listarComentarioValoracion();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetComentarioValoracionByRecetaId_Success() {
        // Arrange
        CommentValueView comment1 = mock(CommentValueView.class);
        CommentValueView comment2 = mock(CommentValueView.class);

        List<CommentValueView> commentViews = List.of(comment1, comment2);
        when(commentValueRepository.findComentarioValoracionByRecetaId(1L)).thenReturn(commentViews);

        // Act
        List<CommentValueView> result = recipeService.getComentarioValoracionByRecetaId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetComentarioValoracionByRecetaId_Empty() {
        // Arrange
        when(commentValueRepository.findComentarioValoracionByRecetaId(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recipeService.getComentarioValoracionByRecetaId(1L);
        });
        assertEquals("Valoraciones y comentarios nulos para receta con id: 1", exception.getMessage());
    }

    @Test
    void testGuardarComentarioValoracion_ValidInput() {
        // Arrange
        CommentValue comment = new CommentValue();
        comment.setComentario("Delicious!");
        comment.setValoracion(5L);

        when(commentValueRepository.save(comment)).thenReturn(comment);

        // Act
        CommentValue result = recipeService.guardarComentarioValoracion(comment);

        // Assert
        assertNotNull(result);
        assertEquals("Delicious!", result.getComentario());
        assertEquals(5L, result.getValoracion());
    }

    @Test
    void testGuardarComentarioValoracion_EmptyComment() {
        // Arrange
        CommentValue comment = new CommentValue();
        comment.setComentario("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recipeService.guardarComentarioValoracion(comment);
        });
        assertEquals("comentario no puede estar vacío.", exception.getMessage());
    }

    @Test
    void testGuardarComentarioValoracion_InvalidRating() {
        // Arrange
        CommentValue comment = new CommentValue();
        comment.setComentario("Good");
        comment.setValoracion(6L); // Invalid rating

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recipeService.guardarComentarioValoracion(comment);
        });
        assertEquals("La valoración de ser entre 1 y 5.", exception.getMessage());
    }

    @Test
    void testAgregarVideo_Success() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Act
        recipeService.agregarVideo(1L, "http://example.com/video.mp4");

        // Assert
        verify(recetaRepository, times(1)).save(recipe);
        assertEquals("http://example.com/video.mp4", recipe.getUrlVideo());
    }

    @Test
    void testAgregarVideo_RecipeNotFound() {
        // Arrange
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recipeService.agregarVideo(1L, "http://example.com/video.mp4");
        });
        assertEquals("busqueda sin exito", exception.getMessage());
    }

    @Test
    void testGetRecetaById_ValidId() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Act
        Recipe result = recipeService.getRecetaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetRecetaById_InvalidId() {
        // Arrange
        when(recetaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recipeService.getRecetaById(1L);
        });
        assertEquals("busqueda sin exito", exception.getMessage());
    }

    @Test
    void testCrearReceta_NullInput() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            recipeService.crearReceta(null);
        });
        assertEquals("Recipe cannot be null", exception.getMessage());
    }

    @Test
    void testCrearReceta_ValidInput() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setNombre("Receta Nueva");

        // Act
        recipeService.crearReceta(recipe);

        // Assert
        verify(recetaRepository, times(1)).save(recipe);
    }

    @Test
    void testDetalleReceta_ComplexData() {
        // Arrange
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDificultad("Alta");
        recipe.setIngredientes("Ingrediente1, Ingrediente2");
        recipe.setInstrucciones("Paso1, Paso2");
        recipe.setTiempoCoccion(30);
        recipe.setFotografiaUrl("http://example.com/image.jpg");
        recipe.setUrlVideo("http://example.com/video.mp4");
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Act
        Map<String, Object> result = recipeService.detalleReceta(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get("id"));
        assertEquals("Alta", result.get("dificultad"));
        assertEquals("Ingrediente1, Ingrediente2", result.get("ingredientes"));
        assertEquals("Paso1, Paso2", result.get("instrucciones"));
        assertEquals(30, result.get("tiempoCoccion"));
        assertEquals("http://example.com/image.jpg", result.get("fotografiaUrl"));
        assertEquals("http://example.com/video.mp4", result.get("urlVideo"));
    }

    @Test
    void testDetalleReceta_EmptyFields() {
        // Arrange
        Recipe receta = new Recipe(); // A recipe with all fields empty
        receta.setId(1L); // Only ID is set
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        // Act
        Map<String, Object> result = recipeService.detalleReceta(1L);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty() || result.values().stream().allMatch(Objects::isNull),
            "Expected result to be empty or contain only null values but found: " + result);
    }

}
