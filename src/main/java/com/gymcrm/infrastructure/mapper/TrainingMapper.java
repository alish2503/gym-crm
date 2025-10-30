package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainingMapper {

    public static TrainingDao toDao(Training training) {
        TraineeDao traineeDao = TraineeMapper.toDao(training.trainee());
        TrainerDao trainerDao = TrainerMapper.toDao(training.trainer());
        TrainingTypeDao trainingTypeDao = new TrainingTypeDao(training.trainer().getSpecialization().name());
        return new TrainingDao(training.trainingName(), training.trainingDate(), training.duration(),
                traineeDao, trainerDao, trainingTypeDao);
    }

    public static Training toDomain(TrainingDao trainingDao) {
        TraineeDao traineeDao = trainingDao.getTrainee();
        Trainee trainee = TraineeMapper.toDomain(traineeDao.getUser(), traineeDao.getDateOfBirth(), traineeDao.getAddress());
        TrainerDao trainerDao = trainingDao.getTrainer();
        TrainingType type = new TrainingType(trainerDao.getSpecialization().getName());
        Trainer trainer = TrainerMapper.toDomain(trainerDao.getUser(), type);
        return new Training(trainingDao.getTrainingName(), trainingDao.getTrainingDate(), trainingDao.getDuration(),
                trainer, trainee);
    }
}
