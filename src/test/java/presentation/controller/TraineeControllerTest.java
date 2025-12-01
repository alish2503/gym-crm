package presentation.controller;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.TraineeService;
import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.model.User;
import com.gymcrm.presentation.controller.impl.TraineeController;
import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTraineeDto;
import com.gymcrm.presentation.dto.request.UpdateTrainersDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersAfterUpdateDto;
import com.gymcrm.presentation.dto.response.TraineeWithTrainersDto;
import com.gymcrm.presentation.dto.response.TrainerDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController controller;

    @Test
    void registerTrainee_shouldReturn201AndBody() {
        CreateTraineeDto dto = new CreateTraineeDto(
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "London"
        );

        UserCredentials credentials = new UserCredentials("John.Doe", "pass");
        when(traineeService.createTrainee(any())).thenReturn(credentials);
        ResponseEntity<UserCredentialsDto> response = controller.registerTrainee(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John.Doe", response.getBody().username());
        assertEquals("pass", response.getBody().password());
        assertEquals(URI.create("/trainees/John.Doe"), response.getHeaders().getLocation());
    }

    @Test
    void getTraineeProfile_shouldReturnDto() {
        User user = new User("John.Doe", "pass", "John", "Doe", true);
        Trainee trainee = new Trainee(user, LocalDate.of(1990, 1, 1), "London");
        Trainer trainer = new Trainer(new User("trainer", "pass", "John",
                "Doe", true), new TrainingType(1L, TrainingTypeEnum.FITNESS));

        trainee.setTrainers(List.of(trainer));
        when(traineeService.getTraineeByUsername("John.Doe")).thenReturn(trainee);
        TraineeWithTrainersDto dto = controller.getTraineeProfile("John.Doe");
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getDateOfBirth());
        assertEquals("London", dto.getAddress());
        assertEquals(1, dto.getTrainerDtos().size());
        assertEquals("trainer", dto.getTrainerDtos().get(0).getUsername());
    }

    @Test
    void updateTraineeProfile_shouldReturnDtoAfterUpdate() {
        UpdateTraineeDto dto = new UpdateTraineeDto("John", "Doe", true,
                LocalDate.of(1990, 1, 1), "London");

        User user = new User("John.Doe", "pass", "John", "Doe", true);
        Trainee trainee = new Trainee(user, LocalDate.of(1990, 1, 1), "London");
        when(traineeService.updateTrainee(any())).thenReturn(trainee);
        TraineeWithTrainersAfterUpdateDto result = controller.updateTraineeProfile("John.Doe", dto);
        assertEquals("John.Doe", result.getUsername());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void deleteTraineeProfile_shouldCallService() {
        controller.deleteTraineeProfile("John.Doe");
        verify(traineeService).deleteTrainee("John.Doe");
    }

    @Test
    void getAvailableTrainers_shouldReturnTrainerDtos() {
        User user = new User("trainer", "pass", "John", "Doe", true);
        Trainer trainer = new Trainer(user, new TrainingType(1L, TrainingTypeEnum.FITNESS));
        when(traineeService.getAvailableTrainersForTrainee("John.Doe")).thenReturn(List.of(trainer));
        List<TrainerDto> trainers = controller.getAvailableTrainers("John.Doe");
        assertEquals(1, trainers.size());
        assertEquals("trainer", trainers.get(0).getUsername());
    }

    @Test
    void updateTrainers_shouldReturnUpdatedTrainerDtos() {
        UpdateTrainersDto dto = new UpdateTrainersDto();
        dto.setTrainerUsernames(List.of("trainer"));
        User user = new User("Mike.Black", "pass", "Mike", "Black", true);
        Trainer trainer = new Trainer(user, new TrainingType(1L, TrainingTypeEnum.FITNESS));
        when(traineeService.updateTrainersForTrainee(eq("John.Doe"), anyList())).thenReturn(List.of(trainer));
        List<TrainerDto> result = controller.updateTrainers("John.Doe", dto);
        assertEquals(1, result.size());
        assertEquals("Mike.Black", result.get(0).getUsername());
    }
}