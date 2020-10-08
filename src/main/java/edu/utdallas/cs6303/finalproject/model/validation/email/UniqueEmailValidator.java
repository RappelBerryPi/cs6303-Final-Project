package edu.utdallas.cs6303.finalproject.model.validation.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueEmail uniqueEmail) {
        // not necessary
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        // email is not null and no current users are using this email
        return email != null && (userRepository.findByEmail(email) == null);
    }
}