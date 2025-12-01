package presentation.controller;

import com.gymcrm.application.service.port.TrainingService;
import com.gymcrm.domain.model.*;
import com.gymcrm.presentation.controller.impl.TrainingController;
import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTraineeDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingForTraineeDto;
import com.gymcrm.presentation.dto.response.TrainingForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingTypeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController controller;

    @Test
    void addTraining_shouldReturnCreated() {
        CreateTrainingDto dto = new CreateTrainingDto();
        dto.setTrainerUsername("Mike.Black");
        dto.setTraineeUsername("John.Doe");
        dto.setTrainingType(TrainingTypeEnum.FITNESS);
        dto.setTrainingName("Morning Yoga");
        dto.setDate(LocalDate.now().plusDays(1));
        dto.setDuration(60);
        controller.addTraining(dto);
        verify(trainingService).createTraining(any());
    }

    @Test
    void getTrainingsForTrainee_shouldReturnList() {
        User trainerUser = new User("Mike.Black", "pass", "Mike", "Black",
                true);

        Trainer trainer = new Trainer(trainerUser, new TrainingType(1L, TrainingTypeEnum.FITNESS));
        Trainee trainee = new Trainee(new User("John.Doe", "pass", "John",
                "Doe", true), LocalDate.of(1990, 1, 1), "London");

        Training training = new Training(new TrainingType(1L, TrainingTypeEnum.FITNESS), "Morning Yoga",
                LocalDate.now().plusDays(1), 60, trainer, trainee);

        when(trainingService.getTrainingsForTrainee(eq("John.Doe"), any())).thenReturn(List.of(training));
        List<TrainingForTraineeDto> result = controller.getTrainingsForTrainee("John.Doe",
                new TrainingFilterForTraineeDto());

        assertEquals(1, result.size());
        assertEquals("Morning Yoga", result.get(0).getTrainingName());
        assertEquals("Mike", result.get(0).getTrainerName().getFirstName());
        assertEquals("Black", result.get(0).getTrainerName().getLastName());
    }

    @Test
    void getTrainingsForTrainer_shouldReturnList() {
        User traineeUser = new User("John.Doe", "pass", "John", "Doe",
                true);

        Trainee trainee = new Trainee(traineeUser, LocalDate.of(1990, 1, 1),
                "London");

        User trainerUser = new User("Mike.Black", "pass", "Mike", "Black",
                true);

        Trainer trainer = new Trainer(trainerUser, new TrainingType(1L, TrainingTypeEnum.FITNESS));

        Training training = new Training(new TrainingType(1L, TrainingTypeEnum.FITNESS), "Morning Yoga",
                LocalDate.now().plusDays(1), 60, trainer, trainee);

        when(trainingService.getTrainingsForTrainer(eq("Mike.Black"), any()))
                .thenReturn(List.of(training));

        List<TrainingForTrainerDto> result = controller.getTrainingsForTrainer("Mike.Black",
                new TrainingFilterForTrainerDto());

        assertEquals(1, result.size());
        assertEquals("Morning Yoga", result.get(0).getTrainingName());
        assertEquals("John", result.get(0).getTraineeName().getFirstName());
        assertEquals("Doe", result.get(0).getTraineeName().getLastName());
    }

    @Test
    void getTrainingTypes_shouldReturnList() {
        TrainingType type = new TrainingType(1L, TrainingTypeEnum.FITNESS);
        when(trainingService.getTrainingTypes()).thenReturn(List.of(type));
        List<TrainingTypeDto> result = controller.getTrainingTypes();
        assertEquals(1, result.size());
        assertEquals("FITNESS", result.get(0).type());
    }

    @Test
    void handleExistingTraining_shouldReturnBadRequest() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate training");
        ResponseEntity<?> response = controller.handleExistingTraining(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Duplicate training"));
    }
}

