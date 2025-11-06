package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.*;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainingMapper {

    public static TrainingDao toDao(Training training) {
        TraineeDao traineeDao = TraineeMapper.toDao(training.getTraineeId());
        TrainerDao trainerDao = TrainerMapper.toDao(training.getTrainerId());
        TrainingTypeDao trainingTypeDao = TrainingTypeMapper.toDao(training.getType());
        return new TrainingDao(training.getName(), training.getDate(), training.getDuration(),
                traineeDao, trainerDao, trainingTypeDao);
    }

    public static Training toDomainForTrainee(TrainingDao dao) {
        Trainer trainer = TrainerMapper.toDomainWithProfile(dao.getTrainer(), dao.getType());
        return new Training(trainer.getSpecialization(), dao.getName(), dao.getDate(),
                dao.getDuration(), trainer);
    }

    public static Training toDomainForTrainer(TrainingDao dao) {
        Trainee trainee = TraineeMapper.toDomainWithProfile(dao.getTrainee());
        TrainingType type = TrainingTypeMapper.toDomain(dao.getType());
        return new Training(type, dao.getName(), dao.getDate(), dao.getDuration(), trainee);
    }
}
