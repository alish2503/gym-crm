package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTraineeRequest;
import com.gymcrm.application.request.UpdateTraineeRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.CredentialService;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.application.service.port.TraineeService;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    public TraineeServiceImpl(TraineeRepository traineeRepository,
                              TrainerRepository trainerRepository,
                              UserProfileRepository userProfileRepository,
                              PasswordEncoder encoder,
                              CredentialService credentialService)
    {
        super(traineeRepository, userProfileRepository, encoder, credentialService);
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainee getTraineeByUsername(String username) {
        return traineeRepository.findTraineeWithTrainers(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No trainee found with username: " + username
                ));
    }

    @Override
    @Transactional
    public UserCredentials createTrainee(CreateTraineeRequest request) {
        Trainee created = new Trainee(request.getDateOfBirth(), request.getAddress());
        return createUser(request, created);
    }

    @Override
    @Transactional
    public Trainee updateTrainee(UpdateTraineeRequest request) {
        String username = request.getUsername();
        Trainee updated = getTraineeByUsername(username);
        updated.setDateOfBirth(request.getDateOfBirth());
        updated.setAddress(request.getAddress());
        updateUser(updated, request);
        return updated;
    }

    @Override
    @Transactional
    public void deleteTrainee(String username) {
        Long id = traineeRepository.findIdByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("No trainee found with username: " + username)
        );
        traineeRepository.deleteById(id);
    }

    @Override
    @Transactional
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
        traineeRepository.update(trainee);
        return trainers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainer> getAvailableTrainersForTrainee(String username) {
        if (!userProfileRepository.existsByUserName(username)) {
            throw new EntityNotFoundException("No trainee found with username: " + username);
        }
        List<Long> assignedIds = trainerRepository.findAssignedTrainersIds(username);
        return assignedIds.isEmpty() ? trainerRepository.findAllActive() :
                trainerRepository.getAvailableTrainersNotAssignedAndActive(assignedIds);
    }
}

