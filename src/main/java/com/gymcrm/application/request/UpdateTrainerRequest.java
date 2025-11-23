package com.gymcrm.application.request;

import com.gymcrm.domain.model.TrainingTypeEnum;

/**
 * @author Alish
 */
public class UpdateTrainerRequest extends UpdateUserRequest {
    private final TrainingTypeEnum specialization;

    public UpdateTrainerRequest(String username, String firstName, String lastName,
                                boolean isActive, TrainingTypeEnum specialization)
    {
        super(username, firstName, lastName, isActive);
        this.specialization = specialization;
    }

    public TrainingTypeEnum getSpecialization() {
        return specialization;
    }
}
