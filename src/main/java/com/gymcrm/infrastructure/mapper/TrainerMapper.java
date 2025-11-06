package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainerMapper {

    public static TrainerDao toDao(Trainer trainer) {
        TrainingTypeDao typeDao = TrainingTypeMapper.toDao(trainer.getSpecialization());
        return new TrainerDao(trainer.getId(), UserMapper.toDao(trainer.getUserProfile()), typeDao);
    }

    public static TrainerDao toDao(Long id) {
        return new TrainerDao(id);
    }

    public static Trainer toDomain(TrainerDao trainerDao) {
        TrainingType type = TrainingTypeMapper.toDomain(trainerDao.getSpecialization());
        return new Trainer(trainerDao.getId(), type);
    }

    public static Trainer toDomainWithProfile(TrainerDao trainerDao) {
        TrainingType type = TrainingTypeMapper.toDomain(trainerDao.getSpecialization());
        return new Trainer(trainerDao.getId(), UserMapper.toDomain(trainerDao.getUser()), type);
    }

    public static Trainer toDomainWithProfile(TrainerDao trainerDao, TrainingTypeDao typeDao) {
        TrainingType type = TrainingTypeMapper.toDomain(typeDao);
        return new Trainer(trainerDao.getId(), UserMapper.toDomain(trainerDao.getUser()), type);
    }
}
