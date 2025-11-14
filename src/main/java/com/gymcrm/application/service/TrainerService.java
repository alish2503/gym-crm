package com.gymcrm.application.service;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.domain.model.Trainer;

/**
 * @author Alish
 */
public interface TrainerService extends UserService {
    Trainer getTrainerByUsername(String username);
    UserCredentials createTrainer(CreateTrainerRequest request);
    Trainer updateTrainer(UpdateUserRequest request);
}
