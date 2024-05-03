package com.evri.interview.controller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomNameValidator implements ConstraintValidator<NameValidation, String> {
    @Override
    public void initialize(NameValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value.split(" ").length == 2;
    }
}
