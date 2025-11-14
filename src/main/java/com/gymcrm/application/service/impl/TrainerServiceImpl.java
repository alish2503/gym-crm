package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.application.service.TrainerService;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alish
 */
@Service
public class TrainerServiceImpl extends UserServiceImpl<Trainer> implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository,
                              TrainingTypeRepository trainingTypeRepository,
                              UserProfileRepository userProfileRepository,
                              CredentialService credentialService)
    {
        super(trainerRepository, userProfileRepository, credentialService, Trainer.class);
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getTrainerByUsername(String username) {
        log.debug("Fetching trainer by username: {}", username);
        return trainerRepository.findTrainerWithTrainees(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainer not found with user name: " + username
                ));
    }

    @Override
    @Transactional
    public UserCredentials createTrainer(CreateTrainerRequest request) {
        TrainingType specialization = findTypeOrThrow(request.getSpecialization());
        Trainer created = new Trainer(specialization);
        return createUser(request, created);
    }

    @Override
    @Transactional
    public Trainer updateTrainer(UpdateUserRequest request) {
        String username = request.getUsername();
        log.info("Updating trainer with username: {}", username);
        Trainer updated = getTrainerByUsername(username);
        updateUser(updated, request);
        log.debug("Trainer {} updated", username);
        return updated;
    }

    private TrainingType findTypeOrThrow(TrainingTypeEnum typeEnum) {
        return trainingTypeRepository.findByName(typeEnum).orElseThrow(
                () -> new EntityNotFoundException("No specialization: " + typeEnum + " found")
        );
    }
}

