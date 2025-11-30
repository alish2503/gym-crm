package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.UpdateTrainerRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.CredentialService;
import com.gymcrm.domain.model.*;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.application.service.port.TrainerService;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class TrainerServiceImpl extends AbstractUserService<Trainer> implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository,
                              TrainingTypeRepository trainingTypeRepository,
                              UserProfileRepository userProfileRepository,
                              PasswordEncoder encoder,
                              CredentialService credentialService)
    {
        super(trainerRepository, userProfileRepository, encoder, credentialService);
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        return trainerRepository.findTrainerWithTrainees(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No trainer found with username: " + username
                ));
    }

    @Override
    public UserCredentials createTrainer(CreateTrainerRequest request) {
        TrainingType specialization = findTypeOrThrow(request.getSpecialization());
        Trainer created = new Trainer(specialization);
        return createUser(request, created);
    }

    @Override
    public Trainer updateTrainer(UpdateTrainerRequest request) {
        String username = request.getUsername();
        Trainer updated = getTrainerByUsername(username);
        TrainingType specialization = findTypeOrThrow(request.getSpecialization());
        updated.setSpecialization(specialization);
        updateUser(updated, request);
        return updated;
    }

    private TrainingType findTypeOrThrow(TrainingTypeEnum typeEnum) {
        return trainingTypeRepository.findByName(typeEnum).orElseThrow(
                () -> new EntityNotFoundException("No specialization: " + typeEnum + " found")
        );
    }
}

