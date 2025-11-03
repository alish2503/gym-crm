package com.gymcrm.application.dto.response;

import com.gymcrm.domain.model.TrainingTypeEnum;

import java.util.List;

/**
 * @author Alish
 */
public class UpdateTrainerResponse extends GetTrainerResponse {
    String userName;

    public UpdateTrainerResponse(String firstName, String lastName, TrainingTypeEnum specialization,
                                 boolean isActive, List<TraineeResponse> trainees, String userName)
    {
        super(firstName, lastName, specialization, isActive, trainees);
        this.userName = userName;
    }
}
