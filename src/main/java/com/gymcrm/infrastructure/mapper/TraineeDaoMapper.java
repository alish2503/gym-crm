package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.infrastructure.dao.TraineeDao;
import com.gymcrm.infrastructure.dao.TrainerDao;

import java.util.List;

/**
 * @author Alish
 */
public class TraineeDaoMapper {

    private TraineeDaoMapper(){}

    public static TraineeDao toDao(Trainee trainee) {
        TraineeDao dao = new TraineeDao(trainee.getId(), UserDaoMapper.toDao(trainee.getUser()),
                trainee.getDateOfBirth(), trainee.getAddress());

        List<Trainer> trainers = trainee.getTrainers();
        if (!trainers.isEmpty()) {
            List<TrainerDao> trainerDaos = trainers.stream().map(TrainerDaoMapper::toDao).toList();
            dao.setTrainers(trainerDaos);
        }
        return dao;
    }

    public static TraineeDao toDao(Long id) {
        return new TraineeDao(id);
    }

    public static Trainee toDomain(TraineeDao dao) {
        return new Trainee(dao.getId(), UserDaoMapper.toDomain(dao.getUser()), dao.getDateOfBirth(), dao.getAddress());
    }
}
