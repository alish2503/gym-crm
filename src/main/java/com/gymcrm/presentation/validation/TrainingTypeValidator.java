package com.gymcrm.presentation.validation;

import com.gymcrm.domain.model.TrainingTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author Alish
 */
public class TrainingTypeValidator implements ConstraintValidator<ValidTrainingType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            TrainingTypeEnum.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
