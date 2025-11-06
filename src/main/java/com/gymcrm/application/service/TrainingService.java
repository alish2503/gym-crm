package com.gymcrm.application.service;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public interface TrainingService {
    void createTraining(Training training);
    List<Training> getTrainingsForTrainee(String traineeUserName, String password, LocalDate from,
                                          LocalDate to, FullName trainerName, TrainingTypeEnum typeEnum);

    List<Training> getTrainingsForTrainer(String trainerUserName, String password, LocalDate from,
                                          LocalDate to, FullName traineeName);
}
