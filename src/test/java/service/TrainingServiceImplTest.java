package service;

import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.request.CreateTrainingRequest;
import com.gymcrm.application.service.AuthService;
import com.gymcrm.application.service.impl.TrainingServiceImpl;
import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.domain.port.TrainingTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private UserCredentials traineeCreds;
    private UserCredentials trainerCreds;

    private User traineeUser;
    private User trainerUser;
    private CreateTrainingRequest request;

    private TrainingType yogaType;

    @BeforeEach
    void setUp() {
        request = new CreateTrainingRequest("trainer1", "trainee1",
                TrainingTypeEnum.YOGA, "Morning Yoga",
                LocalDate.of(2025, 11, 10), 60);

        traineeCreds = new UserCredentials("trainee1", "pass");
        trainerCreds = new UserCredentials("trainer1", "pass");
        traineeUser = new User("trainee1", "hashed", "John", "Doe", true);
        trainerUser = new User("trainer1", "hashed", "Jane", "Smith", true);
        yogaType = new TrainingType(1L, TrainingTypeEnum.YOGA);
    }

    @Test
    void createTraining_shouldSaveTraining() {
        when(trainingRepository.existsTraining("trainer1","trainee1",
                LocalDate.of(2025,11,10),"Morning Yoga")).thenReturn(false);

        when(traineeRepository.findIdByUsername("trainee1")).thenReturn(Optional.of(2L));
        when(trainerRepository.findIdByUsername("trainer1")).thenReturn(Optional.of(1L));
        when(trainingTypeRepository.findByName(TrainingTypeEnum.YOGA)).thenReturn(Optional.of(yogaType));
        trainingService.createTraining(request);
        verify(trainingRepository).save(argThat(training ->
                training.getName().equals("Morning Yoga") &&
                        training.getDuration() == 60 &&
                        training.getType() == yogaType &&
                        training.getTraineeId().equals(2L) &&
                        training.getTrainerId().equals(1L) &&
                        training.getDate().equals(LocalDate.of(2025, 11, 10))
        ));
    }

    @Test
    void createTraining_shouldThrowIfAlreadyExists() {
        when(trainingRepository.existsTraining("trainer1","trainee1",
                LocalDate.of(2025,11,10),"Morning Yoga")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> trainingService.createTraining(request));
    }

    @Test
    void getTrainingsForTrainee_shouldReturnFilteredTrainings() {
        TrainingFilter filter = new TrainingFilter(LocalDate.of(2025,11,1),
                LocalDate.of(2025,11,30), null, TrainingTypeEnum.YOGA);

        Training t1 = new Training(yogaType,"Morning Yoga",
                LocalDate.of(2025,11,10),60,1L,2L);

        Training t2 = new Training(yogaType,"Evening Yoga",
                LocalDate.of(2025,11,15),45,1L,2L);

        when(authService.authenticate("trainee1","pass")).thenReturn(traineeUser);
        when(trainingTypeRepository.existsByName(TrainingTypeEnum.YOGA)).thenReturn(true);
        when(trainingRepository.findTrainingsForTrainee("trainee1", filter)).thenReturn(List.of(t1,t2));
        List<Training> trainings = trainingService.getTrainingsForTrainee(traineeCreds, filter);
        assertEquals(2, trainings.size());
        assertEquals("Morning Yoga", trainings.get(0).getName());
        assertEquals(60, trainings.get(0).getDuration());
        assertEquals("Evening Yoga", trainings.get(1).getName());
    }

    @Test
    void getTrainingsForTrainee_shouldThrowIfTypeNotFound() {
        TrainingFilter filter = new TrainingFilter(null, null, null,TrainingTypeEnum.FITNESS);
        when(authService.authenticate("trainee1","pass")).thenReturn(traineeUser);
        when(trainingTypeRepository.existsByName(TrainingTypeEnum.FITNESS)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> trainingService.getTrainingsForTrainee(traineeCreds, filter));
    }

    @Test
    void getTrainingsForTrainer_shouldReturnTrainings() {
        TrainingFilter filter = new TrainingFilter(null,null,null,null);

        Training t1 = new Training(yogaType,"Morning Yoga",
                LocalDate.of(2025,11,10),60,1L,2L);

        when(authService.authenticate("trainer1","pass")).thenReturn(trainerUser);
        when(trainingRepository.findTrainingsForTrainer("trainer1", filter)).thenReturn(List.of(t1));
        List<Training> trainings = trainingService.getTrainingsForTrainer(trainerCreds, filter);
        assertEquals(1, trainings.size());
        assertEquals("Morning Yoga", trainings.get(0).getName());
        assertEquals(60, trainings.get(0).getDuration());
    }
}
