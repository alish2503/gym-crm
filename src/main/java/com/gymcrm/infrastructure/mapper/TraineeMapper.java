package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;

/**
 * @author Alish
 */
public class TraineeMapper extends UserMapperUtil {
    public static TraineeDao toDao(Trainee trainee) {
        TraineeDao dao = new TraineeDao();
        UserMapperUtil.mapToDaoBase(trainee, dao);
        dao.setDateOfBirth(trainee.getDateOfBirth());
        dao.setAddress(trainee.getAddress());
        return dao;
    }

    public static Trainee ToDomain(TraineeDao dao) {
        return new Trainee(dao.getUsername(), dao.getPassword(), dao.getFirstName(),
                dao.getLastName(), dao.isActive(), dao.getDateOfBirth(), dao.getAddress());
    }
}
