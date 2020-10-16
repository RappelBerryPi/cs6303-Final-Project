package edu.utdallas.cs6303.finalproject.model.validation.username;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;

public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

    private UserRepository userRepository;

    public UniqueUserNameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(UniqueUserName uniqueUserName) {
        // not necessary
    }

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {
        // email is not null and no current users are using this email
        return userName != null && (userRepository.findByUsername(userName) == null);
    }
}