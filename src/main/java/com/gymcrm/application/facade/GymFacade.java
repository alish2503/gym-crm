package com.gymcrm.application.facade;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;

/**
 * @author Alish
 */
public interface GymFacade {
    Trainee createTrainee(Trainee trainee);
    void updateTrainee(Trainee trainee);
    void deleteTrainee(String username);
    Trainee getTraineeByUserName(String userName);
    Trainer createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Trainer getTrainerByUserName(String userName);
    Training createTraining(Training training);
    Training getTrainingById(Long id);
}
