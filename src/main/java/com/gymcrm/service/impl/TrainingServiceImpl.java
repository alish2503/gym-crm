package com.gymcrm.service.impl;

import com.gymcrm.dao.TrainingDao;
import com.gymcrm.exception.EntityNotFoundException;
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
        log.info("Creating training: {} for trainee {} and trainer {}",
                training.getTrainingName(),
                training.getTrainee().getUsername(),
                training.getTrainer().getUsername());

        Training saved = trainingDao.save(training);
        log.debug("Training created successfully: {}", saved);
        return saved;
    }

    @Override
    public Training getById(Long id) {
        log.debug("Fetching training by id: {}", id);
        return trainingDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found: " + id));
    }
}
