package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.UpdateTrainerRequest;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.service.AuthService;
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
class TrainerServiceImpl extends UserServiceImpl<Trainer> implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository,
                              TrainingTypeRepository trainingTypeRepository,
                              UserProfileRepository userProfileRepository,
                              CredentialService credentialService,
                              AuthService authService)
    {
        super(trainerRepository, userProfileRepository, credentialService, authService, Trainer.class);
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Trainer getTrainerByUserName(UserCredentials credentials) {
        String username = credentials.username();
        String password = credentials.password();
        log.debug("Fetching trainer by username: {}", username);
        User authenticated = authService.authenticate(username, password);
        Trainer trainer = findTrainerOrThrow(username);
        trainer.setUserProfile(authenticated);
        return trainer;
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
    public Trainer updateTrainer(UpdateTrainerRequest request, UserCredentials credentials) {
        String username = credentials.username();
        String password = credentials.password();
        log.info("Updating trainer with username: {}", username);
        User authenticated = authService.authenticate(username, password);
        Trainer updated = findTrainerOrThrow(username);
        TrainingType specialization = findTypeOrThrow(request.getSpecialization());
        updated.setSpecialization(specialization);
        updateUser(updated, authenticated, request);
        log.debug("Trainer {} updated", username);
        return updated;
    }

    private TrainingType findTypeOrThrow(TrainingTypeEnum typeEnum) {
        return trainingTypeRepository.findByName(typeEnum).orElseThrow(
                () -> new EntityNotFoundException("No specialization: " + typeEnum + " found")
        );
    }

    protected Trainer findTrainerOrThrow(String username) {
        return trainerRepository.findTrainerWithTrainees(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainer not found with user name: " + username
                ));
    }
}

