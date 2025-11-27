package infrastructure.controller;

import com.gymcrm.application.service.port.AuthService;
import com.gymcrm.presentation.controller.impl.AuthController;
import com.gymcrm.presentation.dto.request.LoginDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController controller;

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
