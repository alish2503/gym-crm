package com.gymcrm.infrastructure.persistence.mapper;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainerDaoMapper {

    private TrainerDaoMapper() {}

    public static TrainerDao toDao(Trainer trainer) {
        TrainingTypeDao typeDao = TrainingTypeDaoMapper.toDao(trainer.getSpecialization());
        return new TrainerDao(trainer.getId(), UserDaoMapper.toDao(trainer.getUser()), typeDao);
    }

    public static TrainerDao toDao(Long id) {
        return new TrainerDao(id);
    }

    public static Trainer toDomain(TrainerDao trainerDao) {
        TrainingType type = TrainingTypeDaoMapper.toDomain(trainerDao.getSpecialization());
        return new Trainer(trainerDao.getId(), UserDaoMapper.toDomain(trainerDao.getUser()), type);
    }
}
