package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;

/**
 * @author Alish
 */
public class TrainerMapper {

    public static TrainerDao toDao(Trainer trainer) {
        TrainerDao dao = new TrainerDao();
        UserMapperUtil.mapToDaoBase(trainer, dao);
        dao.setSpecialization(trainer.getSpecialization().getName().name());
        return dao;
    }

    public static Trainer toDomain(TrainerDao dao, TrainingType specialization) {
        return new Trainer(dao.getUsername(), dao.getPassword(), dao.getFirstName(), dao.getLastName(), dao.isActive(),
                specialization);
    }
}
