package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.port.TrainerWorkloadEventPublisher;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.application.service.TraineeService;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.application.event.ActionType;
import com.gymcrm.application.event.TrainerWorkloadEvent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alish
 */
@Service
public class TraineeServiceImpl extends AbstractUserService<Trainee> implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingRepository trainingRepository;
    private final TrainerWorkloadEventPublisher trainerWorkloadEventPublisher;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository,
                              TrainerRepository trainerRepository,
                              TrainingRepository trainingRepository,
                              TrainerWorkloadEventPublisher trainerWorkloadEventPublisher,
                              UserProfileRepository userProfileRepository,
                              PasswordEncoder encoder,
                              CredentialService credentialService)
    {
        super(traineeRepository, userProfileRepository, encoder, credentialService);
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingRepository = trainingRepository;
        this.trainerWorkloadEventPublisher = trainerWorkloadEventPublisher;
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        return traineeRepository.findTraineeWithTrainers(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No trainee found with username: " + username
                ));
    }

    @Override
    public UserCredentials createTrainee(CreateTraineeRequest request) {
        Trainee created = new Trainee(request.getDateOfBirth(), request.getAddress());
        return createUser(request, created);
    }

    @Override
    public Trainee updateTrainee(UpdateTraineeRequest request) {
        String username = request.getUsername();
        Trainee updated = getTraineeByUsername(username);
        updated.setDateOfBirth(request.getDateOfBirth());
        updated.setAddress(request.getAddress());
        updateUser(updated, request);
        return updated;
    }

    @Override
    public void deleteTrainee(String username) {
        traineeRepository.findTrainee(username).ifPresent(trainee -> {
            TrainingFilter filter = new TrainingFilter(null, null, null, null);
            List<Training> trainings = trainingRepository.findTrainingsForTrainee(username, filter);
            trainings.forEach(t -> {
                Trainer trainer = t.getTrainer();
                trainerWorkloadEventPublisher.publish(new TrainerWorkloadEvent(
                        trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        trainer.getUser().isActive(),
                        t.getDate(),
                        t.getDurationInHours(),
                        ActionType.DELETE
                ));
            });
            trainingRepository.deleteAllTrainingsByTraineeUsername(username);
            traineeRepository.deleteTrainee(trainee);
        });

    }

    @Override
    public List<Trainer> updateTrainersForTrainee(String username, List<String> usernames) {
        Trainee trainee = traineeRepository.findTrainee(username).orElseThrow(
                () -> new EntityNotFoundException("No trainee found with username: " + username)
        );
        List<Trainer> trainers = trainerRepository.findTrainersByUserNamesIn(usernames);
        if (trainers.size() < usernames.size()) {
            Set<String> found = trainers.stream().
                    map(t -> t.getUser().getUsername()).collect(Collectors.toSet());

            List<String> notFound = usernames.stream().filter(name -> !found.contains(name)).toList();
            String errorUsernames = String.join(", ", notFound);
            throw new EntityNotFoundException("No trainers found with usernames: " + errorUsernames);
        }
        trainee.setTrainers(trainers);
        traineeRepository.saveOrUpdate(trainee);
        return trainers;
    }

    @Override
    public List<Trainer> getAvailableTrainersForTrainee(String username) {
        if (!userProfileRepository.existsByUserName(username)) {
            throw new EntityNotFoundException("No trainee found with username: " + username);
        }
        return trainerRepository.findAvailableTrainersNotAssignedAndActive(username);
    }
}

