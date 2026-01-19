package com.gymcrm.application;

import com.gymcrm.application.request.UpdateTrainerRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.impl.TrainerServiceImpl;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private CredentialService credentialService;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private User user;
    private Trainer trainer;
    private TrainingType specialization;

    @BeforeEach
    void setUp() {
        user = new User("John.Doe", "hashed", "John", "Doe", true);
        specialization = new TrainingType(1L, TrainingTypeEnum.YOGA);
        trainer = new Trainer(specialization);
        trainer.setUser(user);
    }

    @Test
    void getTrainerByUserName_shouldReturnTrainer() {
        when(trainerRepository.findTrainerWithTrainees("John.Doe")).thenReturn(Optional.of(trainer));
        Trainer result = trainerService.getTrainerByUsername("John.Doe");
        assertEquals(trainer, result);
        assertEquals(user, result.getUser());
    }

    @Test
    void createTrainer_shouldCallCreateUserAndReturnCredentials() {
        CreateTrainerRequest req = new CreateTrainerRequest(
                "John", "Doe", TrainingTypeEnum.YOGA
        );

        when(trainingTypeRepository.findByName(TrainingTypeEnum.YOGA)).thenReturn(Optional.of(specialization));
        when(credentialService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(credentialService.generatePassword()).thenReturn("pass");
        when(encoder.encode("pass")).thenReturn("hashed");
        UserCredentials credentials = trainerService.createTrainer(req);
        assertEquals("John.Doe", credentials.username());
        assertEquals("pass", credentials.password());
        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository).saveOrUpdate(captor.capture());
        Trainer saved = captor.getValue();
        assertEquals(specialization, saved.getSpecialization());
        assertNotNull(saved.getUser());
        assertEquals("John.Doe", saved.getUser().getUsername());
    }

    @Test
    void updateTrainer_shouldUpdateFields() {
        UpdateTrainerRequest req = new UpdateTrainerRequest(
                "John.Doe", "John", "Doe", true, TrainingTypeEnum.YOGA
        );
        TrainingType specialization = new TrainingType(2L, TrainingTypeEnum.YOGA);

        when(trainerRepository.findTrainerWithTrainees("John.Doe")).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByName(TrainingTypeEnum.YOGA)).thenReturn(Optional.of(specialization));
        Trainer result = trainerService.updateTrainer(req);
        assertEquals(specialization, result.getSpecialization());
        assertEquals(user, result.getUser());
        verify(trainerRepository).saveOrUpdate(trainer);
    }
}
