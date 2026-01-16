package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.application.service.port.TrainerWorkloadProducer;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.application.service.TrainingService;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.application.event.ActionType;
import com.gymcrm.application.event.TrainerWorkloadEvent;
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
    private final TrainerWorkloadProducer trainerWorkloadProducer;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository, TraineeRepository traineeRepository,
                               TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository,
                               UserProfileRepository userProfileRepository,
                               TrainerWorkloadProducer trainerWorkloadProducer)
    {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.userProfileRepository = userProfileRepository;
        this.trainerWorkloadProducer = trainerWorkloadProducer;
    }

    @Override
    public void createTraining(CreateTrainingRequest request) {
        String traineeUsername = request.traineeUsername();
        String trainerUsername = request.trainerUsername();
        String trainingName = request.trainingName();
        int durationInHours = request.durationInHours();
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
        Training created = new Training(type, trainingName, trainingDate, durationInHours, trainer.getId(), traineeId);
        trainingRepository.saveOrUpdate(created);
        trainerWorkloadProducer.sendMessage(new TrainerWorkloadEvent(
                trainerUsername,
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().isActive(),
                trainingDate,
                durationInHours,
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
