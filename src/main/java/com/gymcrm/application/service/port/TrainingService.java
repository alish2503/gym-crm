package com.gymcrm.application.service.port;

import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingType;

import java.util.List;

/**
 * @author Alish
 */
public interface TrainingService {
    void createTraining(CreateTrainingRequest request);
    List<Training> getTrainingsForTrainee(String username, TrainingFilter trainingFilter);
    List<Training> getTrainingsForTrainer(String username, TrainingFilter trainingFilter);
    List<TrainingType> getTrainingTypes();
}
