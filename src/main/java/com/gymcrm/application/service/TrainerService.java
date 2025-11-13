package com.gymcrm.application.service;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.domain.model.Trainer;

/**
 * @author Alish
 */
public interface TrainerService extends UserService {
    Trainer getTrainerByUserName(String username);
    UserCredentials createTrainer(CreateTrainerRequest request);
    Trainer updateTrainer(UpdateUserRequest request);
}
