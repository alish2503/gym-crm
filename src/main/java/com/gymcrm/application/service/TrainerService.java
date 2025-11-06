package com.gymcrm.application.service;

import com.gymcrm.domain.model.Trainer;

/**
 * @author Alish
 */
public interface TrainerService extends UserService {
    Trainer getTrainerByUserName(String username, String password);
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
}
