package com.gymcrm.service.impl;

import com.gymcrm.dao.TrainingDao;
import com.gymcrm.model.Training;
import com.gymcrm.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class TrainingServiceImpl extends BaseServiceImpl<Training, Long> implements TrainingService {

    private final TrainingDao trainingDao;

    @Autowired
    public TrainingServiceImpl(TrainingDao trainingDao) {
        super(trainingDao);
        this.trainingDao = trainingDao;
    }

    @Override
    public Training create(Training training) {
        return trainingDao.save(training);
    }

    @Override
    public Training getById(Long id) {
        return trainingDao.findById(id);
    }
}
