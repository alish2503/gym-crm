package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.persistence.dao.UserDao;

/**
 * @author Alish
 */
public class TrainerMapper extends UserMapperUtil {

    public static TrainerDao toDao(Trainer trainer) {
        TrainingTypeDao typeDao = new TrainingTypeDao(trainer.getSpecialization().name());
        TrainerDao dao = new TrainerDao(typeDao);
        dao.setUser(getUserDao(trainer));
        return dao;
    }

    public static Trainer toDomain(UserDao userDao, TrainingType type) {
        Trainer trainer = new Trainer(type);
        mapToDomainBase(trainer, userDao);
        return trainer;
    }
}
