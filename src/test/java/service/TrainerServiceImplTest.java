package service;

import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.request.CreateTrainerRequest;
import com.gymcrm.application.request.UpdateTrainerRequest;
import com.gymcrm.application.service.AuthService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private AuthService authService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private UserCredentials creds;
    private User user;
    private TrainingType specialization;

    @BeforeEach
    void setUp() {
        creds = new UserCredentials("John.Doe", "pass");
        user = new User("John.Doe", "hashed", "John", "Doe", true);
        specialization = new TrainingType(1L, TrainingTypeEnum.YOGA);
    }

    @Test
    void getTrainerByUserName_shouldReturnTrainerWithAuthenticatedUser() {
        Trainer trainer = new Trainer(specialization);
        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(trainerRepository.findTrainerWithTrainees("John.Doe")).thenReturn(Optional.of(trainer));
        Trainer result = trainerService.getTrainerByUserName(creds);
        assertEquals(trainer, result);
        assertEquals(user, result.getUser());
    }

    @Test
    void getTrainerByUserName_shouldThrowIfIncorrectPassword() {
        when(authService.authenticate("John.Doe", "pass")).thenThrow(new SecurityException());
        assertThrows(SecurityException.class, () -> trainerService.getTrainerByUserName(creds)
        );
    }

    @Test
    void createTrainer_shouldCallCreateUserAndReturnCredentials() {
        CreateTrainerRequest req = new CreateTrainerRequest(
                true, "John", "Doe", TrainingTypeEnum.YOGA
        );

        when(trainingTypeRepository.findByName(TrainingTypeEnum.YOGA)).thenReturn(Optional.of(specialization));
        when(credentialService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(credentialService.generatePassword()).thenReturn("pass");
        when(credentialService.encodePassword("pass")).thenReturn("hashed");
        UserCredentials credentials = trainerService.createTrainer(req);
        assertEquals("John.Doe", credentials.username());
        assertEquals("pass", credentials.password());
        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository).save(captor.capture());
        Trainer saved = captor.getValue();
        assertEquals(specialization, saved.getSpecialization());
        assertNotNull(saved.getUser());
        assertEquals("John.Doe", saved.getUser().getUsername());
    }

    @Test
    void updateTrainer_shouldUpdateFields() {
        Trainer trainer = new Trainer(specialization);
        UpdateTrainerRequest req = new UpdateTrainerRequest(
                "John.Doe", "newPass", "John", "Doe", true, TrainingTypeEnum.YOGA
        );

        when(authService.authenticate("John.Doe", "pass")).thenReturn(user);
        when(trainerRepository.findTrainerWithTrainees("John.Doe")).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByName(TrainingTypeEnum.YOGA)).thenReturn(Optional.of(specialization));
        when(credentialService.encodePassword("newPass")).thenReturn("newHash");
        Trainer result = trainerService.updateTrainer(req, creds);
        assertEquals(specialization, result.getSpecialization());
        assertEquals("newHash", result.getUser().getPassword());
        assertEquals(user, result.getUser());
        verify(trainerRepository).update(trainer);
    }
}
