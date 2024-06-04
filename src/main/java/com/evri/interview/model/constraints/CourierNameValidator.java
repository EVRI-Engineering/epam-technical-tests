package com.evri.interview.model.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.evri.interview.model.constraints.CourierNameValidator.NameValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {NameValidator.class})
public @interface CourierNameValidator {

  String message() default "Name must contain two words, splitted by space";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class NameValidator implements ConstraintValidator<CourierNameValidator, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      return value.split(" ").length == 2;
    }
  }
}
