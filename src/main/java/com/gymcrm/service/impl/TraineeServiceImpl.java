package com.gymcrm.service.impl;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class TraineeServiceImpl extends UserServiceImpl<Trainee> implements TraineeService {

    private final TraineeDao traineeDao;

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao) {
        super(traineeDao);
        this.traineeDao = traineeDao;
    }

    @Override
    public void delete(String username) {
        traineeDao.delete(username);
    }
}

