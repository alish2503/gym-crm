package com.gymcrm.application.service;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.UpdateTrainerRequest;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.domain.model.Trainer;

/**
 * @author Alish
 */
public interface TrainerService extends UserService {
    Trainer getTrainerByUserName(UserCredentials credentials);
    UserCredentials createTrainer(CreateTrainerRequest request);
    Trainer updateTrainer(UpdateTrainerRequest request, UserCredentials credentials);
}
