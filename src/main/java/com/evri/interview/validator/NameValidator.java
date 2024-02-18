package com.evri.interview.validator;

import com.evri.interview.validator.annotation.FullName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<FullName, String> {
    @Override
    public void initialize(FullName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null && name.matches("^\\p{L}+\\s+\\p{L}+$");
    }
}
