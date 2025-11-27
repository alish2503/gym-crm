package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.dao.TraineeDao;
import com.gymcrm.infrastructure.dao.TrainerDao;
import com.gymcrm.infrastructure.dao.TrainingDao;
import com.gymcrm.infrastructure.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainingDaoMapper {

    private TrainingDaoMapper() {}

    public static TrainingDao toDao(Training training) {
        TraineeDao traineeDao = TraineeDaoMapper.toDao(training.getTraineeId());
        TrainerDao trainerDao = TrainerDaoMapper.toDao(training.getTrainerId());
        TrainingTypeDao trainingTypeDao = TrainingTypeDaoMapper.toDao(training.getType());
        return new TrainingDao(training.getName(), training.getDate(), training.getDuration(),
                traineeDao, trainerDao, trainingTypeDao);
    }

    public static Training toDomainForTrainee(TrainingDao dao) {
        Trainer trainer = TrainerDaoMapper.toDomain(dao.getTrainer());
        TrainingType type = TrainingTypeDaoMapper.toDomain(dao.getType());
        return new Training(type, dao.getName(), dao.getDate(), dao.getDuration(), trainer);
    }

    public static Training toDomainForTrainer(TrainingDao dao) {
        Trainee trainee = TraineeDaoMapper.toDomain(dao.getTrainee());
        TrainingType type = TrainingTypeDaoMapper.toDomain(dao.getType());
        return new Training(type, dao.getName(), dao.getDate(), dao.getDuration(), trainee);
    }
}
