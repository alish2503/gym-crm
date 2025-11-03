package com.gymcrm.application.service;

import com.gymcrm.application.dto.request.CreateTraineeRequest;
import com.gymcrm.application.dto.request.UpdateTraineeRequest;
import com.gymcrm.application.dto.request.UpdateTrainersRequest;
import com.gymcrm.application.dto.response.GetTraineeResponse;
import com.gymcrm.application.dto.response.TrainerResponse;
import com.gymcrm.application.dto.response.UpdateTraineeResponse;
import com.gymcrm.application.dto.request.UserRequest;

import java.util.List;

/**
 * @author Alish
 */
public interface TraineeService extends UserService {
    GetTraineeResponse getTraineeByUserName(String traineeUserName);
    UserRequest createTrainee(CreateTraineeRequest request);
    UpdateTraineeResponse updateTrainee(UpdateTraineeRequest request);
    void deleteTrainee(String username);
    List<TrainerResponse> updateTrainersForTrainee(UpdateTrainersRequest request);
}
