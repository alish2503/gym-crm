package com.gymcrm.application.dto.request;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.TrainingTypeEnum;

/**
 * @author Alish
 */
public class CreateTrainerRequest extends FullName {
    private TrainingTypeEnum specialization;

    public CreateTrainerRequest(String firstName, String lastName, TrainingTypeEnum specialization) {
        super(firstName, lastName);
        this.specialization = specialization;
    }

    public CreateTrainerRequest() {}

    public void setSpecialization(TrainingTypeEnum specialization) {
        this.specialization = specialization;
    }
}
