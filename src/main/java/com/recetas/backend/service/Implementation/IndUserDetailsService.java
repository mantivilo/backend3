package com.recetas.backend.service.Implementation;
import com.recetas.backend.model.Users;
import com.recetas.backend.repository.IUserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Service
public class IndUserDetailsService implements UserDetailsService {
    Logger logger
            = LoggerFactory.getLogger(IndUserDetailsService.class);

    private final IUserRepository userRepository;

    public IndUserDetailsService(IUserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        Users user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

}
