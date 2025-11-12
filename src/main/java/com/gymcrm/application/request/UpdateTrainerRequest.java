package com.gymcrm.application.request;

import com.gymcrm.domain.model.TrainingTypeEnum;

/**
 * @author Alish
 */
public class UpdateTrainerRequest extends UpdateUserRequest {
    private TrainingTypeEnum specialization;

    public UpdateTrainerRequest(String username, String password, String firstName, String lastName,
                                boolean isActive, TrainingTypeEnum specialization)
    {
        super(username, password, firstName, lastName, isActive);
        this.specialization = specialization;
    }

    public TrainingTypeEnum getSpecialization() {
        return specialization;
    }
}
