package com.gymcrm.application.service;

import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;

import java.util.List;

/**
 * @author Alish
 */
public interface TrainingService {
    void createTraining(CreateTrainingRequest request);
    List<Training> getTrainingsForTrainee(UserCredentials credentials, TrainingFilter trainingFilter);
    List<Training> getTrainingsForTrainer(UserCredentials credentials, TrainingFilter trainingFilter);
}
