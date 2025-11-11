package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;

import java.util.List;

/**
 * @author Alish
 */
public class TraineeMapper {
    public static TraineeDao toDao(Trainee trainee) {
        TraineeDao dao = new TraineeDao(trainee.getId(), UserMapper.toDao(trainee.getUser()),
                trainee.getDateOfBirth(), trainee.getAddress());

        List<Trainer> trainers = trainee.getTrainers();
        if (!trainers.isEmpty()) {
            List<TrainerDao> trainerDaos = trainers.stream().map(TrainerMapper::toDao).toList();
            dao.setTrainers(trainerDaos);
        }
        return dao;
    }

    public static TraineeDao toDao(Long id) {
        return new TraineeDao(id);
    }

    public static Trainee toDomain(TraineeDao dao) {
        return new Trainee(dao.getId(), dao.getDateOfBirth(), dao.getAddress());
    }

    public static Trainee toDomainWithProfile(TraineeDao dao) {
        return new Trainee(dao.getId(), UserMapper.toDomain(dao.getUser()), dao.getDateOfBirth(), dao.getAddress());
    }

}
