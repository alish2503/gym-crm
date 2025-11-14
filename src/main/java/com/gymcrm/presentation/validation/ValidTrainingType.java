package com.gymcrm.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Alish
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TrainingTypeValidator.class)
public @interface ValidTrainingType {
    String message() default "Invalid training type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
