package com.gymcrm.application.request;

import com.gymcrm.domain.model.TrainingTypeEnum;

/**
 * @author Alish
 */
public class CreateTrainerRequest extends CreateUserRequest {
    private TrainingTypeEnum specialization;

    public CreateTrainerRequest(boolean isActive, String firstName, String lastName,
                                TrainingTypeEnum specialization)
    {
        super(isActive, firstName, lastName);
        this.specialization = specialization;
    }

    public TrainingTypeEnum getSpecialization() {
        return specialization;
    }
}
