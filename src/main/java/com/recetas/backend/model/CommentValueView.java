package com.recetas.backend.model;

public interface CommentValueView {
    Long getId();
    String getComentario();
    Long getValoracion();
    Long getRecetaId(); // Ahora coincide con la consulta JPQL
}
