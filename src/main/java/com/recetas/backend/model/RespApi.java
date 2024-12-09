package com.recetas.backend.model;

import lombok.Data;

@Data
public class RespApi {

    private String message;
    private boolean success;

    public RespApi(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
