package edu.utdallas.cs6303.finalproject.services.user;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.User;

@Service
public class SimpleAuthenticationManager implements AuthenticationManager {

    @Autowired
    private UserServiceInterface userService;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> userOptional = userService.findByUserName((String) authentication.getPrincipal());
        if (userOptional.isPresent() && userService.isPasswordValid(userOptional.get(), (String) authentication.getCredentials())) {
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userService.getAuthorities(userOptional.get()));
        }
        return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials());
    }
    
}
