package infrastructure.controller;

import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.AuthService;
import com.gymcrm.application.service.port.TraineeService;
import com.gymcrm.application.service.port.TrainerService;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.presentation.controller.impl.AuthController;
import com.gymcrm.presentation.dto.request.CreateTraineeDto;
import com.gymcrm.presentation.dto.request.CreateTrainerDto;
import com.gymcrm.presentation.dto.request.LoginDto;
import com.gymcrm.presentation.dto.response.UserCredentialsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;

import java.net.URI;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController controller;

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
    void registerTrainer_shouldReturn201AndBody() {
        CreateTrainerDto dto = new CreateTrainerDto("John", "Doe", TrainingTypeEnum.YOGA);
        UserCredentials credentials = new UserCredentials("John.Doe", "pass");
        when(trainerService.createTrainer(any())).thenReturn(credentials);
        ResponseEntity<UserCredentialsDto> response = controller.registerTrainer(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John.Doe", response.getBody().username());
        assertEquals(URI.create("/trainers/John.Doe"), response.getHeaders().getLocation());
    }

    @Test
    void login_shouldReturnToken() {
        LoginDto dto = new LoginDto();
        dto.setUsername("John.Doe");
        dto.setPassword("pass");
        when(authService.login("John.Doe", "pass")).thenReturn("token123");
        ResponseEntity<Map<String, String>> response = controller.login(dto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("accessToken", "token123"), response.getBody());
    }

    @Test
    void logout_shouldCallServiceAndReturnOk() {
        ResponseEntity<Void> response = controller.logout("Bearer abc");
        verify(authService).logout("Bearer abc");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void handleLocked_shouldReturn429() {
        LockedException ex = new LockedException("Too many attempts");
        ResponseEntity<Map<String, String>> response = controller.handleLocked(ex);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals(Map.of("error", "Too many attempts"), response.getBody());
    }

    @Test
    void handleLogout_shouldReturn401() {
        InsufficientAuthenticationException ex = new InsufficientAuthenticationException("Not authenticated");
        ResponseEntity<Map<String, String>> response = controller.handleLogout(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Map.of("error", "Not authenticated"), response.getBody());
    }
}
