package com.gymcrm.application.dto.response;

import com.gymcrm.application.dto.request.CreateTrainerRequest;
import com.gymcrm.domain.model.TrainingTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public class GetTrainerResponse extends CreateTrainerRequest {
    boolean isActive;
    List<TraineeResponse> trainees = new ArrayList<>();

    public GetTrainerResponse(String firstName, String lastName, TrainingTypeEnum specialization,
                              boolean isActive, List<TraineeResponse> trainees)
    {
        super(firstName, lastName, specialization);
        this.isActive = isActive;
        this.trainees = trainees;
    }
}
