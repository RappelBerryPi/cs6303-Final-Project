package edu.utdallas.cs6303.finalproject.services.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceInterface userService;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        Optional<User> user = userService.findByUserName(userName);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("");
        }
    }

}
