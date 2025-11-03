package com.gymcrm.application.service;

import com.gymcrm.application.dto.request.CreateTrainerRequest;
import com.gymcrm.application.dto.request.UpdateTrainerRequest;
import com.gymcrm.application.dto.request.UserRequest;
import com.gymcrm.application.dto.response.*;

import java.util.List;

/**
 * @author Alish
 */
public interface TrainerService extends UserService {
    GetTrainerResponse getTrainerByUserName(String trainerUserName);
    UserRequest createTrainer(CreateTrainerRequest request);
    UpdateTrainerResponse updateTrainer(UpdateTrainerRequest request);
    List<TrainerResponse> getAvailableTrainersForTrainee(String traineeUserName);
}
