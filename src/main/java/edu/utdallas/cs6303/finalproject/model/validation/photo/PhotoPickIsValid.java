package edu.utdallas.cs6303.finalproject.model.validation.photo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhotoPickValidation.class )
public @interface PhotoPickIsValid {
    String message() default "Photo does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}