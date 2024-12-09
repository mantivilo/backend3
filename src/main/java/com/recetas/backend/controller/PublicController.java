package com.recetas.backend.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.recetas.backend.model.Users;
import com.recetas.backend.service.RecipeService;
import com.recetas.backend.service.UserManagementService;
import java.util.Map;
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:8082")
public class PublicController {

     private final RecipeService recetaService;
    private final UserManagementService userService;

    public PublicController(RecipeService recetaService, UserManagementService userService) {
        this.recetaService = recetaService;
        this.userService = userService;
    }
    
    @GetMapping("/home")
    public Map<String, Object> getHomePage() {
        List<Map<String, Object>> recetasRecientes = recetaService.getRecetasRecientes();
        List<Map<String, Object>> recetasPopulares = recetaService.getRecetasPopulares();
        List<String> banners = List.of("Banner 1", "Banner 2");

        Map<String, Object> response = new HashMap<>();
        response.put("recetasRecientes", recetasRecientes);
        response.put("recetasPopulares", recetasPopulares);
        response.put("banners", banners);

        return response;
    }

    @GetMapping("/buscar")
    public List<Map<String, Object>> buscarRecetas(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "tipoCocina", required = false) String tipoCocina,
            @RequestParam(value = "paisOrigen", required = false) String paisOrigen,
            @RequestParam(value = "dificultad", required = false) String dificultad) {

        return recetaService.buscarRecetas(nombre, tipoCocina, paisOrigen, dificultad);
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {
        try {
            userService.createUserAccount(user);
            return ResponseEntity.ok("Usuario Ingresado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de registro: " + e.getMessage());
        }
    }
}