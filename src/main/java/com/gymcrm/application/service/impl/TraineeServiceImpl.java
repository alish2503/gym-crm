package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.AuthService;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.application.service.TraineeService;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alish
 */
@Service
class TraineeServiceImpl extends UserServiceImpl<Trainee> implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository,
                              TrainerRepository trainerRepository,
                              UserProfileRepository userProfileRepository,
                              CredentialService credentialService,
                              AuthService authService)
    {
        super(traineeRepository, userProfileRepository, credentialService, authService);
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Trainee getTraineeByUserName(String username, String password) {
        log.debug("Fetching trainee by username: {}", username);
        User authenticated = authService.authenticate(username, password);
        Trainee trainee = findTraineeOrThrow(username);
        trainee.setUserProfile(authenticated);
        return trainee;
    }

    @Override
    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        User userProfile = trainee.getUserProfile();
        log.info("Creating new trainee: {} {}", userProfile.getFirstName(), userProfile.getLastName());
        setCredentials(userProfile);
        Trainee created = new Trainee(trainee.getUserProfile(), trainee.getDateOfBirth(), trainee.getAddress());
        traineeRepository.save(created);
        log.info("Trainee created successfully with username: {}", userProfile.getUsername());
        return created;
    }

    @Override
    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        User userProfile = trainee.getUserProfile();
        String username = userProfile.getUsername();
        log.info("Updating trainee with username: {}", username);
        User authenticated = authService.authenticate(username, userProfile.getPassword());
        Trainee updated = findTraineeOrThrow(username);
        updated.setDateOfBirth(trainee.getDateOfBirth());
        updated.setAddress(trainee.getAddress());
        updated.setUserProfile(authenticated);
        updateFullNameAndSave(updated, userProfile.getFirstName(), userProfile.getLastName());
        log.debug("Trainee {} updated", username);
        return updated;
    }

    @Override
    @Transactional
    public void deleteTrainee(String username, String password) {
        log.info("Deleting trainee with username: {}", username);
        authService.authenticate(username, password);
        traineeRepository.delete(username);
        log.debug("Trainee {} deleted", username);
    }

    @Override
    @Transactional
    public List<Trainer> updateTrainersForTrainee(String username, String password, List<String> usernames) {
        log.info("Updating trainers for trainee with username: {}", username);
        User authenticated = authService.authenticate(username, password);
        Trainee trainee = findTraineeOrThrow(username);
        List<Trainer> trainers = trainerRepository.findTrainersByUserNamesIn(usernames);
        if (trainers.size() < usernames.size()) {
            Set<String> found = trainers.stream().
                    map(t -> t.getUserProfile().getUsername()).collect(Collectors.toSet());

            List<String> notFound = usernames.stream().filter(name -> !found.contains(name)).toList();
            String errorMessage = String.join(", ", notFound);
            throw new IllegalArgumentException("Trainers with user names: " + errorMessage + "not found");
        }
        trainee.setTrainers(trainers);
        trainee.setUserProfile(authenticated);
        traineeRepository.update(trainee);
        log.debug("Trainers for trainee {} updated", username);
        return trainers;
    }

    @Override
    public List<Trainer> getAvailableTrainersForTrainee(String username, String password) {
        log.debug("Fetching trainers for trainee with username: {}", username);
        authService.authenticate(username, password);
        List<Long> assignedIds = trainerRepository.findAssignedTrainersIds(username);
        return assignedIds.isEmpty() ? trainerRepository.findAll() :
                trainerRepository.getAvailableTrainersNotAssigned(assignedIds);
    }

    protected Trainee findTraineeOrThrow(String username) {
        return traineeRepository.findTraineeWithTrainers(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainee not found with user name: " + username
                ));
    }
}

