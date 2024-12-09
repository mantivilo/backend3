package com.recetas.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RespApiTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String message = "Operation successful";
        boolean success = true;

        // Act
        RespApi respApi = new RespApi(message, success);

        // Assert
        assertEquals(message, respApi.getMessage());
        assertTrue(respApi.isSuccess());
    }

    @Test
    void testSetters() {
        // Arrange
        RespApi respApi = new RespApi("Initial message", false);

        // Act
        respApi.setMessage("Updated message");
        respApi.setSuccess(true);

        // Assert
        assertEquals("Updated message", respApi.getMessage());
        assertTrue(respApi.isSuccess());
    }
}
