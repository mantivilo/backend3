package com.recetas.backend.service;
import com.recetas.backend.model.CommentValue;
import com.recetas.backend.model.CommentValueView;
import com.recetas.backend.repository.CommentValueRepository;

import org.springframework.stereotype.Service;
import java.util.Map;

import java.util.HashMap;
import java.util.List;


import com.recetas.backend.repository.RecetaRepository;

import io.jsonwebtoken.lang.Collections;

import com.recetas.backend.model.Recipe;

@Service
public class RecipeService {

    private final RecetaRepository recetaRepository;
    private final CommentValueRepository commentValueRepository;
    private static final String RECETA_NO_ENCONTRADA = "busqueda sin exito";
   
    public RecipeService(RecetaRepository recetaRepository,CommentValueRepository commentValueRepository) {
        this.recetaRepository = recetaRepository;
        this.commentValueRepository = commentValueRepository;
    }

    public List<Recipe> listarRecetas(){

        return (List<Recipe>) recetaRepository.findAll();
    }
    public List<Map<String, Object>> getRecetasRecientes() {
        return recetaRepository.findRecetasRecientes()
                .stream()
                .map(this::convertToMap)
                .toList();
    }

    public List<Map<String, Object>> getRecetasPopulares() {
        return recetaRepository.findRecetasPopulares()
                .stream()
                .map(this::convertToMap)
                .toList();
    }

    private Map<String, Object> convertToMap(Object[] recetaData) {
        return Map.of(
                "id", recetaData[0],
                "nombre", recetaData[1],
                "paisOrigen", recetaData[2],
                "tipoCocina", recetaData[3],
                "dificultad", recetaData[4]
        );
    }

    public List<Map<String, Object>> buscarRecetas(String nombre, String tipoCocina, String paisOrigen, String dificultad) {
        return recetaRepository.findRecetasByFields(
            nombre, tipoCocina, paisOrigen, dificultad)
            .stream()
            .map(this::convertToMap)
            .toList();
    }

    public Recipe getRecetaById(Long id) {
        return recetaRepository.findById(id).orElseThrow(() -> new RuntimeException(RECETA_NO_ENCONTRADA));
    }

  
    public Map<String, Object> detalleReceta(Long id) {
        Recipe receta = recetaRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException(RECETA_NO_ENCONTRADA));
    
        // Check if all fields are null or empty
        if (receta.getDificultad() == null && receta.getIngredientes() == null &&
            receta.getInstrucciones() == null && receta.getTiempoCoccion() == 0 &&
            receta.getFotografiaUrl() == null && receta.getUrlVideo() == null) {
            return new HashMap<>();
        }
    
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("id", receta.getId());
        detalles.put("dificultad", receta.getDificultad());
        detalles.put("ingredientes", receta.getIngredientes());
        detalles.put("instrucciones", receta.getInstrucciones());
        detalles.put("tiempoCoccion", receta.getTiempoCoccion());
        detalles.put("fotografiaUrl", receta.getFotografiaUrl());
        detalles.put("urlVideo", receta.getUrlVideo());
    
        return detalles;
    }
    
    public void crearReceta(Recipe receta) {
        if (receta == null) {
            throw new NullPointerException("Recipe cannot be null");
        }
        recetaRepository.save(receta);
    }    

    public List<CommentValue> listarComentarioValoracion(){

        return (List<CommentValue>) commentValueRepository.findAll();
    }

    public List<CommentValueView> getComentarioValoracionByRecetaId(Long id) {
        List<CommentValueView> comentarios = commentValueRepository.findComentarioValoracionByRecetaId(id);
        if (comentarios.isEmpty()) {
            throw new IllegalArgumentException("Valoraciones y comentarios nulos para receta con id: " + id);
        }
        return comentarios;
    }

    public CommentValue guardarComentarioValoracion(CommentValue body) {
        if (body.getComentario() == null || body.getComentario().isEmpty()) {
            throw new IllegalArgumentException("comentario no puede estar vacío.");
        }
    
        if (body.getValoracion() == null || body.getValoracion() < 1 || body.getValoracion() > 5) {
            throw new IllegalArgumentException("La valoración de ser entre 1 y 5.");
        }
    
        return commentValueRepository.save(body);
    }

    public void agregarVideo(Long recetaId, String videoUrl) {
        Recipe receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException(RECETA_NO_ENCONTRADA));
        receta.setUrlVideo(videoUrl);
        recetaRepository.save(receta);
    }




}