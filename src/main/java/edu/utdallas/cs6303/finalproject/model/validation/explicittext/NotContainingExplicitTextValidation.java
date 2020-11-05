package edu.utdallas.cs6303.finalproject.model.validation.explicittext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotContainingExplicitTextValidation implements ConstraintValidator<NotContainingExplicitText, String> {

    private List<String> disallowedWords;

    @Override
    public void initialize(NotContainingExplicitText constraintAnnotation) {
        disallowedWords = new ArrayList<>();
        Collections.addAll(disallowedWords, constraintAnnotation.disallowedWords());
        Collections.addAll(disallowedWords, constraintAnnotation.additionalDisallowedWords());
        Predicate<String> p = String::isBlank;
        p.negate();
        disallowedWords = disallowedWords.stream().filter(d -> !d.isBlank()).map(String::toLowerCase).collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.isBlank()) {
            return true;
        }
        final String lowercaseValue = value.toLowerCase();
        return !disallowedWords.stream().anyMatch(s -> lowercaseValue.contains(s));
    }
}