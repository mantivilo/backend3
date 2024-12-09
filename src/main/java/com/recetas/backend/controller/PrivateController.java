package com.recetas.backend.controller;


import com.recetas.backend.model.CommentValue;
import com.recetas.backend.model.CommentValueView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.recetas.backend.model.Recipe;
import com.recetas.backend.service.RecipeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/private") 
@CrossOrigin(origins = "http://localhost:8082")
public class PrivateController {
    
    private final RecipeService recetaService;

    
    public PrivateController(RecipeService recetaService) {
        this.recetaService = recetaService;
    }

    @GetMapping("/recetas")
    public List<Recipe> listarRecetas() {
        return  recetaService.listarRecetas();
    }

    @GetMapping("/receta/{id}")
    public Recipe getRecetaDetails(@PathVariable Long id) {

        return recetaService.getRecetaById(id);
    }

    @GetMapping("/recetas/{id}/detalle")
    public Map<String, Object> getDetalleReceta(@PathVariable Long id) {

        return recetaService.detalleReceta(id);
    }

    @PostMapping("/publicar")
    public ResponseEntity<String> crearReceta(@RequestBody Recipe nuevaReceta) {
        if (nuevaReceta == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se recibió el cuerpo de la receta.");
        }
        try {
            recetaService.crearReceta(nuevaReceta);
            return ResponseEntity.status(HttpStatus.CREATED).body("Receta creada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la receta: " + e.getMessage());
        }
    }

    @GetMapping("/receta/{id}/comentariosValoracion")
    public List<CommentValueView> listarCometariosValoracion(@PathVariable Long id) {
        try {
            return  recetaService.getComentarioValoracionByRecetaId(id);
        }catch (Exception ex){
            return new ArrayList<>();
        }
    }

    @PostMapping("/recetas/{id}/guardarComentarioValoracion")
    public ResponseEntity<CommentValueView> guardarComentarioValoracion(
            @PathVariable Long id, 
            @RequestBody CommentValue comentario) {

     
        if (comentario == null || comentario.getComentario() == null || comentario.getComentario().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (comentario.getValoracion() == null || comentario.getValoracion() < 1 || comentario.getValoracion() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Recipe receta = recetaService.getRecetaById(id);
        if (receta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); 
        }

        comentario.setRecipe(receta);
        try {
   
            CommentValue savedComentario = recetaService.guardarComentarioValoracion(comentario);

            CommentValueView comentarioView = new CommentValueView() {
                @Override
                public Long getId() {
                    return savedComentario.getId();
                }

                @Override
                public String getComentario() {
                    return savedComentario.getComentario();
                }

                @Override
                public Long getValoracion() {
                    return savedComentario.getValoracion();
                }

                @Override
                public Long getRecetaId() {
                    return savedComentario.getRecipe().getId();
                }
            };

            return ResponseEntity.status(HttpStatus.CREATED).body(comentarioView);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); 
        }
    }
    @PostMapping("/recetas/{id}/agregarVideo")
    public ResponseEntity<String> agregarVideo(@PathVariable Long id, @RequestParam String videoUrl) {
        if (videoUrl == null || videoUrl.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error de ingreso: URL del video no puede estar vacía.");
        }

        try {
            recetaService.agregarVideo(id, videoUrl);
            return ResponseEntity.ok("URL del video ingresada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error de ingreso: " + e.getMessage());
        }
    }

}
