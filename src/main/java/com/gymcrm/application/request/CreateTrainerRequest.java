package com.gymcrm.application.request;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.TrainingTypeEnum;
import lombok.Getter;

/**
 * @author Alish
 */
@Getter
public class CreateTrainerRequest extends FullName {
    private final TrainingTypeEnum specialization;

    public CreateTrainerRequest(String firstName, String lastName, TrainingTypeEnum specialization)
    {
        super(firstName, lastName);
        this.specialization = specialization;
    }

}
