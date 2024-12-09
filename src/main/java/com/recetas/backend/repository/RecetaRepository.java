package com.recetas.backend.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.recetas.backend.model.*;
import org.springframework.data.repository.query.Param;
public interface RecetaRepository extends CrudRepository<Recipe, Long> {
    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Recipe r ORDER BY r.id DESC")
    List<Object[]> findRecetasRecientes();

    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Recipe r ORDER BY r.id DESC")
    List<Object[]> findRecetasPopulares();

    @Query("SELECT new Recipe(r.nombre, r.tipoCocina, r.paisOrigen, r.dificultad) " +
            "FROM Recipe r " +
            "WHERE (:nombre IS NULL OR r.nombre = :nombre) " +
            "AND (:tipoCocina IS NULL OR r.tipoCocina = :tipoCocina) " +
            "AND (:paisOrigen IS NULL OR r.paisOrigen = :paisOrigen) " +
            "AND (:dificultad IS NULL OR r.dificultad = :dificultad)")
    List<Recipe> findByNombreAndTipoCocinaAndPaisOrigenAndDificultad(
            @Param("nombre") String nombre,
            @Param("tipoCocina") String tipoCocina,
            @Param("paisOrigen") String paisOrigen,
            @Param("dificultad") String dificultad
    );

    @Query("SELECT r.id, r.nombre, r.paisOrigen, r.tipoCocina, r.dificultad FROM Recipe r " +
            "WHERE (:nombre IS NULL OR :nombre = '' OR r.nombre LIKE %:nombre%) " +
            "AND (:tipoCocina IS NULL OR :tipoCocina = '' OR r.tipoCocina = :tipoCocina) " +
            "AND (:paisOrigen IS NULL OR :paisOrigen = '' OR r.paisOrigen = :paisOrigen) " +
            "AND (:dificultad IS NULL OR :dificultad = '' OR r.dificultad = :dificultad)")
    List<Object[]> findRecetasByFields(
            @Param("nombre") String nombre,
            @Param("tipoCocina") String tipoCocina,
            @Param("paisOrigen") String paisOrigen,
            @Param("dificultad") String dificultad);



}
