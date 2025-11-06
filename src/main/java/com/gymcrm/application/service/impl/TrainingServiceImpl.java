package com.gymcrm.application.service.impl;

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
    public void createTraining(Training training) {
        String traineeUsername = training.getTrainee().getUserProfile().getUsername();
        String trainerUsername = training.getTrainer().getUserProfile().getUsername();
        String trainingName = training.getName();
        log.info("Creating training: {} for trainee {} and trainer {}", trainingName, traineeUsername, trainerUsername);
        LocalDate trainingDate = training.getDate();
        if (trainingRepository.existsTraining(trainerUsername, traineeUsername, trainingDate, trainingName)) {
            throw new IllegalArgumentException("Training already exits");
        }
        Long traineeId = traineeRepository.findIdByUsername(traineeUsername).orElseThrow(
                () -> new EntityNotFoundException("No trainee found with user name: " + traineeUsername)
        );
        Long trainerId = trainerRepository.findIdByUsername(trainerUsername).orElseThrow(
                () -> new EntityNotFoundException("No trainer found with user name: " + trainerUsername)
        );
        TrainingTypeEnum typeEnum = training.getType().name();
        TrainingType type = trainingTypeRepository.findByName(typeEnum).orElseThrow(
                () -> new EntityNotFoundException("No training type: " + typeEnum + " found")
        );
        Training created = new Training(type, trainingName, trainingDate, training.getDuration(),
                trainerId, traineeId);

        log.info("Training created successfully: {}", trainingName);
        trainingRepository.save(created);
    }

    @Override
    public List<Training> getTrainingsForTrainee(String traineeUsername, String password, LocalDate from, LocalDate to,
                                                 FullName trainerName, TrainingTypeEnum typeEnum)
    {
        log.debug("Fetching trainings by trainee username: {}", traineeUsername);
        authService.authenticate(traineeUsername, password);
        if (!trainingTypeRepository.existsByName(typeEnum)) {
            throw new EntityNotFoundException("No training type: " + typeEnum + "found");
        }
        return trainingRepository.findTrainingsForTrainee(traineeUsername,from, to, trainerName, typeEnum);
    }

    @Override
    public List<Training> getTrainingsForTrainer(String trainerUsername, String password, LocalDate from, LocalDate to,
                                                 FullName traineeName)
    {
        log.debug("Fetching trainings by trainer username: {}", trainerUsername);
        authService.authenticate(trainerUsername, password);
        return trainingRepository.findTrainingsForTrainer(trainerUsername, from, to, traineeName);
    }


}
