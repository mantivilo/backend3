package com.recetas.backend.controller;

import com.recetas.backend.security.JWTAuthtenticationConfig;
import com.recetas.backend.service.Implementation.IndUserDetailsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final JWTAuthtenticationConfig jwtAuthtenticationConfig;
    private final IndUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    
    public LoginController(JWTAuthtenticationConfig jwtAuthtenticationConfig, IndUserDetailsService userDetailsService,PasswordEncoder passwordEncoder) {
        this.jwtAuthtenticationConfig = jwtAuthtenticationConfig;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String password) {



        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (isPasswordEncrypted(userDetails.getPassword())) {

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario y/o clave incorrecto");
            }
        } else {

            if (!userDetails.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Usuario y/o clave incorrecto");
            }
        }

        String token = jwtAuthtenticationConfig.getJWTToken(username);

        return ResponseEntity.ok(token);

    }

    private boolean isPasswordEncrypted(String password) {
        // Lógica para verificar si una contraseña está encriptada
        return password.startsWith("$2a$");
    }

}
