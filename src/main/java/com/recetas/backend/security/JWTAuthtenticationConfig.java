package com.recetas.backend.security;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.recetas.backend.constants.Constants.KEY;
import static com.recetas.backend.constants.Constants.getSigningKey;

@Configuration
public class JWTAuthtenticationConfig {

        public String getJWTToken(String username) {
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                        .commaSeparatedStringToAuthorityList("ROLE_USER");

                Map<String, Object> claims = new HashMap<>();
                claims.put("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList());

                String token = Jwts.builder()
                        .setClaims(claims)  // Use setClaims instead of claims
                        .setSubject(username)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1440))
                        .signWith(getSigningKey(KEY))
                        .compact();

                return "Bearer " + token;
        }
                


        }

