package com.recetas.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        //Valida the Spring app context carga bien
    }

    @Test
    void testMainMethod() {
        // Use mockStatic to mock SpringApplication.run
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Act
            BackendApplication.main(new String[]{});

            // Assert
            mockedSpringApplication.verify(() ->
                SpringApplication.run(BackendApplication.class, new String[]{}));
        }
    }
}
