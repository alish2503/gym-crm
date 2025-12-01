package presentation.controller;

import com.gymcrm.application.service.port.UserService;
import com.gymcrm.infrastructure.security.service.port.CustomUserDetailsService;
import com.gymcrm.presentation.controller.impl.UserController;
import com.gymcrm.presentation.dto.request.ChangePasswordDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private UserController controller;

    @Test
    void changeActivity_shouldToggleAndReturnOk() {
        controller.changeActivity("John.Doe");
        verify(userService).toggle("John.Doe");
    }

    @Test
    void setNewPassword_wrongOldPassword_shouldReturn401() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setOldPassword("wrong");
        dto.setNewPassword("new");
        when(customUserDetailsService.isValidPassword("John.Doe", "wrong")).thenReturn(false);
        ResponseEntity<?> response = controller.setNewPassword("John.Doe", dto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Map.of("error", "Wrong password"), response.getBody());
        verify(userService, never()).changePassword(any(), any());
    }

    @Test
    void setNewPassword_correctOldPassword_shouldChangeAndReturnOk() {
        ChangePasswordDto dto = new ChangePasswordDto();
        dto.setOldPassword("old");
        dto.setNewPassword("new");
        when(customUserDetailsService.isValidPassword("John.Doe", "old")).thenReturn(true);
        ResponseEntity<?> response = controller.setNewPassword("John.Doe", dto);
        verify(userService).changePassword("John.Doe", "new");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
