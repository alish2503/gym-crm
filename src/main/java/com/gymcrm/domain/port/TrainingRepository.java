package com.gymcrm.domain.port;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public interface TrainingRepository extends BaseRepository<Training> {
    List<Training> findTrainingsForTrainee(String username, TrainingFilter trainingFilter);
    List<Training> findTrainingsForTrainer(String username, TrainingFilter trainingFilter);
    boolean existsTraining(String trainerUsername, String traineeUsername, LocalDate trainingDate, String trainingName);
}
