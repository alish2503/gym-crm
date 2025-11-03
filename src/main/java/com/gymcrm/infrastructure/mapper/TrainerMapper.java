package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainerMapper extends UserMapperUtil {

    public static TrainerDao toDao(Trainer trainer) {
        TrainingTypeDao typeDao = TrainingTypeMapper.toDao(trainer.getSpecialization());
        return new TrainerDao(trainer.getId(), getUserDao(trainer.getUser()), typeDao);
    }

    public static Trainer toDomain(TrainerDao trainerDao) {
        TrainingType type = TrainingTypeMapper.toDomain(trainerDao.getSpecialization());
        return new Trainer(trainerDao.getId(), getUser(trainerDao.getUser()), type);
    }

    public static Trainer toDomain(TrainerDao trainerDao, TrainingTypeDao typeDao) {
        TrainingType type = TrainingTypeMapper.toDomain(typeDao);
        return new Trainer(trainerDao.getId(), getUser(trainerDao.getUser()), type);

    }
}
