package com.recetas.backend.service;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserManagementDetailsService {

     UserDetails loadUserByUsername(String username);
}
