package com.recetas.backend.repository;

import com.recetas.backend.model.CommentValue;
import com.recetas.backend.model.CommentValueView;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentValueRepository extends CrudRepository<CommentValue, Long> {

    @Query("SELECT c.id AS id, c.comentario AS comentario, c.valoracion AS valoracion, c.recipe.id AS recipeId " +
            "FROM CommentValue c WHERE c.recipe.id = :id")
    List<CommentValueView> findComentarioValoracionByRecetaId(@Param("id") Long recipeId);

}
