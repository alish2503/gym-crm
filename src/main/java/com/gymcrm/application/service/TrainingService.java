package com.gymcrm.application.service;

import com.gymcrm.application.dto.request.AddTrainingRequest;
import com.gymcrm.application.dto.response.TrainingForTraineeResponse;
import com.gymcrm.application.dto.response.TrainingForTrainerResponse;
import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public interface TrainingService {
    void createTraining(AddTrainingRequest request);
    List<TrainingForTraineeResponse> getTrainingsForTrainee(String traineeUserName, LocalDate from,
                                                            LocalDate to, FullName trainerName,
                                                            TrainingTypeEnum typeEnum);

    List<TrainingForTrainerResponse> getTrainingsForTrainer(String trainerUserName, LocalDate from,
                                                            LocalDate to, FullName traineeName);
}
