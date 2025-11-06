package com.gymcrm.application.service;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;

import java.util.List;

/**
 * @author Alish
 */
public interface TraineeService extends UserService {
    Trainee getTraineeByUserName(String username, String password);
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(String username, String password);
    List<Trainer> updateTrainersForTrainee(String username, String password, List<String> usernames);
    List<Trainer> getAvailableTrainersForTrainee(String username, String password);
}
