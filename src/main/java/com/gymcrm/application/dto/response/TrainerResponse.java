package com.gymcrm.application.dto.response;

import com.gymcrm.application.dto.request.CreateTrainerRequest;
import com.gymcrm.domain.model.TrainingTypeEnum;

/**
 * @author Alish
 */
public class TrainerResponse extends CreateTrainerRequest {
    private String userName;

    public TrainerResponse(String firstName, String lastName, TrainingTypeEnum specialization, String userName) {
        super(firstName, lastName, specialization);
        this.userName = userName;
    }
}
