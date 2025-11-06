package com.gymcrm.application.facade;

import com.gymcrm.domain.model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public interface GymFacade {
    void changePasswordForTrainee(String username, String oldPassword, String newPassword);
    void activateTrainee(String username, String password);
    void deactivateTrainee(String username, String password);
    void changePasswordForTrainer(String username, String oldPassword, String newPassword);
    void activateTrainer(String username, String password);
    void deactivateTrainer(String username, String password);
    Trainee getTraineeByUsername(String username, String password);
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    void deleteTrainee(String username, String password);
    List<Trainer> updateTrainersForTrainee(String username, String password, List<String> usernames);
    List<Trainer> getAvailableTrainersForTrainee(String username, String password);
    Trainer getTrainerByUsername(String username, String password);
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    void createTraining(Training training);
    List<Training> getTrainingsForTrainee(String traineeUserName, String password, LocalDate from,
                                          LocalDate to, FullName trainerName, TrainingTypeEnum typeEnum);

    List<Training> getTrainingsForTrainer(String trainerUserName, String password, LocalDate from,
                                          LocalDate to, FullName traineeName);
}
