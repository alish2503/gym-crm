package com.gymcrm.application.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public class UpdateTraineeResponse extends GetTraineeResponse {
    String userName;

    public UpdateTraineeResponse(String firstName, String lastName, LocalDate dateOfBirth, String address,
                                 boolean isActive, List<TrainerResponse> trainers, String userName)
    {
        super(firstName, lastName, dateOfBirth, address, isActive, trainers);
        this.userName = userName;
    }
}
