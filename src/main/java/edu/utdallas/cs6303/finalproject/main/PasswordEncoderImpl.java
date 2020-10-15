package edu.utdallas.cs6303.finalproject.main;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderImpl extends BCryptPasswordEncoder {
    public PasswordEncoderImpl() {
        super(10, new SecureRandom());
    }
}