package com.evri.interview.validator.annotation;

import com.evri.interview.validator.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FullName {
    String message() default "Incorrect fullName";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
