package com.gymcrm.application.service.impl;

import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.application.service.AuthService;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.application.service.TrainingService;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.port.TrainingTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
@Service
public class TrainingServiceImpl implements TrainingService {
    protected final Logger log = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final AuthService authService;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository,
                               TrainingTypeRepository trainingTypeRepository, AuthService authService)
    {

        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public void createTraining(CreateTrainingRequest request) {
        String traineeUsername = request.traineeUsername();
        String trainerUsername = request.trainerUsername();
        String trainingName = request.trainingName();
        int duration = request.duration();
        TrainingTypeEnum typeEnum = request.type();
        LocalDate trainingDate = request.date();
        log.info("Creating training: {} for trainee {} and trainer {}", trainingName, traineeUsername, trainerUsername);
        if (trainingRepository.existsTraining(trainerUsername, traineeUsername, trainingDate, trainingName)) {
            throw new IllegalArgumentException("Training already exits");
        }
        Long traineeId = traineeRepository.findIdByUsername(traineeUsername).orElseThrow(
                () -> new EntityNotFoundException("No trainee found with username: " + traineeUsername)
        );
        Long trainerId = trainerRepository.findIdByUsername(trainerUsername).orElseThrow(
                () -> new EntityNotFoundException("No trainer found with username: " + trainerUsername)
        );
        TrainingType type = trainingTypeRepository.findByName(typeEnum).orElseThrow(
                () -> new EntityNotFoundException("No training type: " + typeEnum + " found")
        );
        Training created = new Training(type, trainingName, trainingDate, duration, trainerId, traineeId);
        log.info("Training created successfully: {}", trainingName);
        trainingRepository.save(created);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsForTrainee(UserCredentials credentials, TrainingFilter trainingFilter)
    {
        String traineeUsername = credentials.username();
        String password = credentials.password();
        TrainingTypeEnum typeEnum = trainingFilter.type();
        log.debug("Fetching trainings by trainee username: {}", traineeUsername);
        authService.authenticate(traineeUsername, password);
        if (typeEnum != null && !trainingTypeRepository.existsByName(typeEnum)) {
            throw new EntityNotFoundException("No training type: " + typeEnum + " found");
        }
        return trainingRepository.findTrainingsForTrainee(traineeUsername, trainingFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsForTrainer(UserCredentials credentials, TrainingFilter trainingFilter)
    {
        String trainerUsername = credentials.username();
        String password = credentials.password();
        log.debug("Fetching trainings by trainer username: {}", trainerUsername);
        authService.authenticate(trainerUsername, password);
        return trainingRepository.findTrainingsForTrainer(trainerUsername, trainingFilter);
    }
}
