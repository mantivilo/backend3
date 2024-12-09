package com.recetas.backend.constants;

import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testGetSigningKeyB64() {
        // Arrange: Use a valid Base64 key with at least 256 bits
        String validBase64Key = "dGhpc2lzY29ycmVjdGtleWZvclRlc3RzMTIzNDU2Nzg5"; // Decodes to 32 bytes

        // Act
        Key key = Constants.getSigningKeyB64(validBase64Key);

        // Assert
        assertNotNull(key, "Key should not be null");
    }

    @Test
    void testGetSigningKey() {
        // Arrange: Use a plain text key with at least 32 characters
        String validPlainTextKey = "this-is-a-32-character-valid-key";

        // Act
        Key key = Constants.getSigningKey(validPlainTextKey);

        // Assert
        assertNotNull(key, "Key should not be null");
    }

    @Test
    void testPrivateConstructorThrowsException() {
        try {
            // Use reflection to access the private constructor
            var constructor = Constants.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            // Act
            constructor.newInstance();
            fail("Expected UnsupportedOperationException");
        } catch (Exception e) {
            // Assert the root cause of the exception
            Throwable cause = e.getCause(); // Get the underlying cause
            assertNotNull(cause);
            assertTrue(cause instanceof UnsupportedOperationException);
            assertEquals("Esta clase no puede ser instanciada", cause.getMessage());
        }
    }

    @Test
    void testConstantsValues() {
        // Assert constant values
        assertEquals("/login", Constants.LOGIN_URL);
        assertEquals("Authorization", Constants.HEADER_AUTHORIZACION_KEY);
        assertEquals("Bearer ", Constants.TOKEN_BEARER_PREFIX);
        assertEquals("https://www.duocuc.cl/", Constants.ISSUER_INFO);
        assertEquals(864_000_000, Constants.TOKEN_EXPIRATION_TIME);
    }
}
