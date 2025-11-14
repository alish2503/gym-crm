package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.application.service.TrainingService;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.domain.port.UserProfileRepository;
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
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository,
                               TrainingTypeRepository trainingTypeRepository,
                               UserProfileRepository userProfileRepository)
    {

        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userProfileRepository = userProfileRepository;
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
    public List<Training> getTrainingsForTrainee(String username, TrainingFilter trainingFilter)
    {
        if (!userProfileRepository.existsByUserName(username)) {
            throw new EntityNotFoundException("No trainee found with user name: " + username);
        }
        TrainingTypeEnum typeEnum = trainingFilter.type();
        log.debug("Fetching trainings by trainee username: {}", username);
        if (typeEnum != null && !trainingTypeRepository.existsByName(typeEnum)) {
            throw new EntityNotFoundException("No training type: " + typeEnum + " found");
        }
        return trainingRepository.findTrainingsForTrainee(username, trainingFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Training> getTrainingsForTrainer(String username, TrainingFilter trainingFilter)
    {
        if (!userProfileRepository.existsByUserName(username)) {
            throw new EntityNotFoundException("No trainer found with user name: " + username);
        }
        log.debug("Fetching trainings by trainer username: {}", username);
        return trainingRepository.findTrainingsForTrainer(username, trainingFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingType> getTrainingTypes() {
        return trainingTypeRepository.findAll();
    }
}
