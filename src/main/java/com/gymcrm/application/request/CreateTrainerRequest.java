package com.gymcrm.application.request;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.TrainingTypeEnum;

/**
 * @author Alish
 */
public class CreateTrainerRequest extends FullName {
    private TrainingTypeEnum specialization;

    public CreateTrainerRequest(String firstName, String lastName, TrainingTypeEnum specialization)
    {
        super(firstName, lastName);
        this.specialization = specialization;
    }

    public TrainingTypeEnum getSpecialization() {
        return specialization;
    }
}
