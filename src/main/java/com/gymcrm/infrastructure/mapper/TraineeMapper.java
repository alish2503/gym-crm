package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.persistence.dao.UserDao;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TraineeMapper extends UserMapperUtil {
    public static TraineeDao toDao(Trainee trainee) {
        TraineeDao dao = new TraineeDao(trainee.getDateOfBirth(), trainee.getAddress());
        dao.setUser(getUserDao(trainee));
        return dao;
    }

    public static Trainee toDomain(UserDao userDao, LocalDate dateOfBirth, String address) {
        Trainee trainee = new Trainee(dateOfBirth, address);
        mapToDomainBase(trainee, userDao);
        return trainee;
    }
}
