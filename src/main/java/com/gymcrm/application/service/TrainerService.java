package com.gymcrm.application.service;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.UpdateTrainerRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.domain.model.Trainer;

/**
 * @author Alish
 */
public interface TrainerService {
    Trainer getTrainerByUsername(String username);
    UserCredentials createTrainer(CreateTrainerRequest request);
    Trainer updateTrainer(UpdateTrainerRequest request);
}
