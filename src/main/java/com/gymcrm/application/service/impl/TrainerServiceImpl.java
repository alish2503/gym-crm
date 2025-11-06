package com.gymcrm.application.service.impl;

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
        super(trainerRepository, userProfileRepository, credentialService, authService);
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public Trainer getTrainerByUserName(String username, String password) {
        log.debug("Fetching trainer by username: {}", username);
        User authenticated = authService.authenticate(username, password);
        Trainer trainer = findTrainerOrThrow(username);
        trainer.setUserProfile(authenticated);
        return trainer;
    }

    @Override
    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        User userProfile = trainer.getUserProfile();
        log.info("Creating new trainer: {} {}", userProfile.getFirstName(), userProfile.getLastName());
        TrainingType specialization = findType(trainer.getSpecialization().name());
        setCredentials(userProfile);
        Trainer created = new Trainer(trainer.getUserProfile(), specialization);
        trainerRepository.save(created);
        log.info("Trainer created successfully with username: {}", userProfile.getUsername());
        return created;
    }

    @Override
    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        User userProfile = trainer.getUserProfile();
        String username = userProfile.getUsername();
        log.info("Updating trainer with username: {}", username);
        User authenticated = authService.authenticate(username, userProfile.getPassword());
        Trainer updated = findTrainerOrThrow(username);
        updated.setUserProfile(authenticated);
        updateFullNameAndSave(updated, userProfile.getFirstName(), userProfile.getLastName());
        log.debug("Trainer {} updated", username);
        return updated;
    }

    private TrainingType findType(TrainingTypeEnum typeEnum) {
        return trainingTypeRepository.findByName(typeEnum).orElseThrow(
                () -> new EntityNotFoundException("No specialization: " + typeEnum + "found")
        );
    }

    protected Trainer findTrainerOrThrow(String username) {
        return trainerRepository.findTrainerWithTrainees(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainer not found with user name: " + username
                ));
    }
}

