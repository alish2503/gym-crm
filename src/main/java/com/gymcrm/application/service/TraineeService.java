package com.gymcrm.application.service;

import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;

import java.util.List;

/**
 * @author Alish
 */
public interface TraineeService extends UserService {
    Trainee getTraineeByUserName(UserCredentials credentials);
    UserCredentials createTrainee(CreateTraineeRequest request);
    Trainee updateTrainee(UpdateTraineeRequest request, UserCredentials credentials);
    void deleteTrainee(UserCredentials credentials);
    List<Trainer> updateTrainersForTrainee(UserCredentials credentials, List<String> usernames);
    List<Trainer> getAvailableTrainersForTrainee(UserCredentials credentials);
}
