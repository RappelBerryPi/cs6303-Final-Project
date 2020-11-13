package edu.utdallas.cs6303.finalproject.main;

import java.util.Optional;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;

import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.services.user.UserServiceInterface;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private GoogleAuthenticator googleAuthenticator;

    @Autowired
    private UserServiceInterface userService;

    @Override
    public Authentication authenticate(Authentication auth) {
        String verificationCode = ((CustomWebAuthenticationDetails) auth.getDetails()).getVerificationCode();
        Optional<User>   userOptional             = userService.findByUserName(auth.getName());
        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("Invalid username or password");
        }
        try {
            if (!googleAuthenticator.authorizeUser(userOptional.get().getUsername(), Integer.parseInt(verificationCode))) {
                throw new BadCredentialsException("Invalid verification code");
            }
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("Please enter a verification code");

        }
        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(userOptional.get(), result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}