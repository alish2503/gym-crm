package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingMapper {

    public static TrainingDao toDao(Training training) {
        String traineeUsername = training.trainee().getUsername();
        String trainerUsername = training.trainer().getUsername();
        String trainingTypeName = training.type().getName().name();
        String trainingName = training.trainingName();
        LocalDate date = training.trainingDate();
        int duration = training.duration();
        return new TrainingDao(traineeUsername, trainerUsername, trainingTypeName, trainingName,
                date, duration);
    }

    public static Training toDomain(TrainingDao dao, Trainer trainer, Trainee trainee) {
        return new Training(dao.getTrainingName(), trainer.getSpecialization(), dao.getTrainingDate(), dao.getDuration(),
                trainer, trainee);
    }
}
