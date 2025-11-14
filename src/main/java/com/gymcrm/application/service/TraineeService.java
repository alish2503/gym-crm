package com.gymcrm.application.service;

import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;

import java.util.List;

/**
 * @author Alish
 */
public interface TraineeService extends UserService {
    Trainee getTraineeByUsername(String username);
    UserCredentials createTrainee(CreateTraineeRequest request);
    Trainee updateTrainee(UpdateTraineeRequest request);
    void deleteTrainee(String username);
    List<Trainer> updateTrainersForTrainee(String username, List<String> usernames);
    List<Trainer> getAvailableTrainersForTrainee(String username);
}
