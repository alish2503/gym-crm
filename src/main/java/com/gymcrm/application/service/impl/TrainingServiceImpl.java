package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.application.service.port.TrainingService;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.infrastructure.port.TrainerWorkloadClient;
import com.gymcrm.presentation.dto.request.ActionType;
import com.gymcrm.presentation.dto.request.TrainerWorkloadEventDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserProfileRepository userProfileRepository;
    private final TrainerWorkloadClient workloadClient;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository,
                               UserProfileRepository userProfileRepository,
                               TrainerWorkloadClient workloadClient)
    {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userProfileRepository = userProfileRepository;
        this.workloadClient = workloadClient;
    }

    @Override
    public void createTraining(CreateTrainingRequest request) {
        String traineeUsername = request.traineeUsername();
        String trainerUsername = request.trainerUsername();
        String trainingName = request.trainingName();
        int duration = request.duration();
        TrainingTypeEnum typeEnum = request.type();
        LocalDate trainingDate = request.date();
        if (trainingRepository.existsTraining(trainerUsername, traineeUsername, trainingDate, trainingName)) {
            throw new DataIntegrityViolationException("Training already exits");
        }
        Long traineeId = traineeRepository.findTraineeId(traineeUsername).orElseThrow(
                () -> new EntityNotFoundException("No trainee found with username: " + traineeUsername)
        );
        Trainer trainer = trainerRepository.findTrainer(trainerUsername).orElseThrow(
                () -> new EntityNotFoundException("No trainer found with username: " + trainerUsername)
        );
        TrainingType type = trainingTypeRepository.findByName(typeEnum).orElseThrow(
                () -> new EntityNotFoundException("No training type: " + typeEnum + " found")
        );
        Training created = new Training(type, trainingName, trainingDate, duration, trainer.getId(), traineeId);
        trainingRepository.saveOrUpdate(created);
        workloadClient.sendEvent(new TrainerWorkloadEventDto(
                trainerUsername,
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().isActive(),
                trainingDate,
                duration,
                ActionType.ADD
        ));
    }

    @Override
    public List<Training> getTrainingsForTrainee(String username, TrainingFilter trainingFilter) {
        if (!userProfileRepository.existsByUserName(username)) {
            throw new EntityNotFoundException("No trainee found with username: " + username);
        }
        TrainingTypeEnum typeEnum = trainingFilter.type();
        if (typeEnum != null && !trainingTypeRepository.existsByName(typeEnum)) {
            throw new EntityNotFoundException("No training type: " + typeEnum + " found");
        }
        return trainingRepository.findTrainingsForTrainee(username, trainingFilter);
    }

    @Override
    public List<Training> getTrainingsForTrainer(String username, TrainingFilter trainingFilter) {
        if (!userProfileRepository.existsByUserName(username)) {
            throw new EntityNotFoundException("No trainer found with username: " + username);
        }
        return trainingRepository.findTrainingsForTrainer(username, trainingFilter);
    }

    @Override
    public List<TrainingType> getTrainingTypes() {
        return trainingTypeRepository.findAll();
    }
}
