package com.recetas.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.recetas.backend.model.Users;
import com.recetas.backend.repository.IUserRepository;

@Service
public class UserManagementService {

    private final IUserRepository repository;
    private final PasswordEncoder encoder;

    public UserManagementService(IUserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void createUserAccount(Users user) {
       
        if (repository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe.");
        }

        
        if (repository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
    }
}
