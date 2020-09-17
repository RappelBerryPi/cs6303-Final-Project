package edu.utdallas.cs6303.finalproject.model.validation.password;

import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class StrongPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    PasswordValidator validator;

    @Override
    public void initialize(ValidPassword arg0) {
        validator = new PasswordValidator(new LengthRule(8, 30), new CharacterRule(EnglishCharacterData.UpperCase, 1), new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1), new CharacterRule(EnglishCharacterData.Special, 1), new WhitespaceRule());
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        String messages = validator.getMessages(result).stream().collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messages).addConstraintViolation().disableDefaultConstraintViolation();
        return false;
    }
}