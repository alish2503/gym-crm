package com.gymcrm.application.service.impl;

import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.application.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class TrainingServiceImpl extends BaseServiceImpl<Training> implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainingServiceImpl(
            TrainingRepository trainingRepository,
            TraineeRepository traineeRepository,
            TrainerRepository trainerRepository) {

        super(trainingRepository);
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Training create(Training training) {
        log.info("Creating training: {} for trainee {} and trainer {}",
                training.trainingName(),
                training.trainee().getUsername(),
                training.trainer().getUsername());

        trainerRepository.findByUsername(training.trainer().getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainer not found: " + training.trainer().getUsername()));

        if (!training.trainer().getSpecialization().getName().name().equals(training.type().getName().name())) {
            throw new IllegalStateException("Training type doesn't match with trainer specialization!");
        }
        traineeRepository.findByUsername(training.trainee().getUsername())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainee not found: " + training.trainee().getUsername()));

        Training saved = trainingRepository.save(training);
        log.info("Training created successfully: {}", saved.trainingName());
        return saved;
    }

    @Override
    public Training getById(Long id) {
        log.debug("Fetching training by id: {}", id);
        return trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training not found: " + id));
    }
}
