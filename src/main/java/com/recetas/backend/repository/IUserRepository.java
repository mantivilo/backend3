package com.recetas.backend.repository;

import com.recetas.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<Users, Integer> {


    Users findOneByUsername(String username);

 
    boolean existsByUsername(String username);

    
    boolean existsByEmail(String email);
}

