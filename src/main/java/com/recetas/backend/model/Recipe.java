package com.recetas.backend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@Entity // Marca esta clase como una entidad de JPA para mapear con una tabla en la base de datos
public class Recipe {

    @Id // Define el campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera el ID automáticamente en la base de datos
    private Long id;
    private String nombre;
    private String tipoCocina;
    private String paisOrigen;
    private String dificultad;
    private String ingredientes;
    private String instrucciones;
    private int tiempoCoccion;
    private String fotografiaUrl;
    private String urlVideo;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<CommentValue> comentariosValoraciones;

    public Recipe() {
        // Empty Constructor
    }

    public Recipe(String nombre, String paisOrigen, String tipoCocina, String dificultad) {

        this.nombre = nombre;
        this.tipoCocina = tipoCocina;
    }
}
