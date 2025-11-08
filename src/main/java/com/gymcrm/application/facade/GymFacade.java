package com.gymcrm.application.facade;

import com.gymcrm.application.request.*;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.domain.model.*;

import java.util.List;

/**
 * @author Alish
 */
public interface GymFacade {
    void changePasswordForTrainee(UserCredentials credentials, String newPassword);
    void activateTrainee(UserCredentials credentials);
    void deactivateTrainee(UserCredentials credentials);
    void changePasswordForTrainer(UserCredentials credentials, String newPassword);
    void activateTrainer(UserCredentials credentials);
    void deactivateTrainer(UserCredentials credentials);
    Trainee getTraineeByUsername(UserCredentials credentials);
    UserCredentials createTrainee(CreateTraineeRequest request);
    Trainee updateTrainee(UpdateTraineeRequest request, UserCredentials credentials);
    void deleteTrainee(UserCredentials credentials);
    List<Trainer> updateTrainersForTrainee(UserCredentials credentials, List<String> usernames);
    List<Trainer> getAvailableTrainersForTrainee(UserCredentials credentials);
    Trainer getTrainerByUsername(UserCredentials credentials);
    UserCredentials createTrainer(CreateTrainerRequest request);
    Trainer updateTrainer(UpdateTrainerRequest request, UserCredentials credentials);
    void createTraining(CreateTrainingRequest request);
    List<Training> getTrainingsForTrainee(UserCredentials credentials, TrainingFilter trainingFilter);
    List<Training> getTrainingsForTrainer(UserCredentials credentials, TrainingFilter trainingFilter);
}
