package com.gymcrm.facade;

import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;

import java.util.List;

/**
 * @author Alish
 */
public interface GymFacade {
    Trainee createTrainee(Trainee trainee);
    void updateTrainee(Trainee trainee);
    void deleteTrainee(String username);
    Trainee getTraineeByUserName(String userName);
    List<Trainee> getAllTrainees();

    Trainer createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Trainer getTrainerByUserName(String userName);
    List<Trainer> getAllTrainers();

    Training createTraining(Training training);
    Training getTrainingById(Long id);
    List<Training> getAllTrainings();
}
