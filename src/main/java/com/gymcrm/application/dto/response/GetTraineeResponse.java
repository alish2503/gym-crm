package com.gymcrm.application.dto.response;

import com.gymcrm.application.dto.request.CreateTraineeRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public class GetTraineeResponse extends CreateTraineeRequest {
    private boolean isActive;
    List<TrainerResponse> trainers = new ArrayList<>();

    public GetTraineeResponse(String firstName, String lastName, LocalDate dateOfBirth, String address,
                              boolean isActive, List<TrainerResponse> trainers)
    {
        super(firstName, lastName, dateOfBirth, address);
        this.isActive = isActive;
        this.trainers = trainers;
    }
}
