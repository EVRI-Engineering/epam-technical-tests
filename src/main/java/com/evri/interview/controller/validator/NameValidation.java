package com.evri.interview.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomNameValidator.class)
public @interface NameValidation {
    String message() default "Name must contain two words, splitted by space";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
