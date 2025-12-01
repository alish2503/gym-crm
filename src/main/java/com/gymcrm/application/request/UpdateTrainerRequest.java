package com.gymcrm.application.request;

import com.gymcrm.domain.model.TrainingTypeEnum;
import lombok.Getter;

/**
 * @author Alish
 */
@Getter
public class UpdateTrainerRequest extends UpdateUserRequest {
    private final TrainingTypeEnum specialization;

    public UpdateTrainerRequest(String username, String firstName, String lastName,
                                boolean isActive, TrainingTypeEnum specialization)
    {
        super(username, firstName, lastName, isActive);
        this.specialization = specialization;
    }

}
