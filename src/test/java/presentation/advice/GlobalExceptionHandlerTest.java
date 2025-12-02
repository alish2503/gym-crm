package presentation.advice;

import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.presentation.advice.GlobalExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tools.jackson.databind.exc.InvalidFormatException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("User with id=5 not found");
        ResponseEntity<Map<String, String>> response = handler.handleNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Entity not found", response.getBody().get("error"));
        assertEquals("User with id=5 not found", response.getBody().get("message"));
    }

    @Test
    void shouldHandleAccessDenied() {
        AuthorizationDeniedException ex = new AuthorizationDeniedException("Forbidden");
        assertDoesNotThrow(() -> handler.handleAccessDenied(ex));
    }

    @Test
    void shouldHandleBadCredentials() {
        BadCredentialsException ex = new BadCredentialsException("Wrong password");
        ResponseEntity<Map<String, String>> response = handler.handleAuthentication(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Wrong password", response.getBody().get("error"));
    }

    @Test
    void shouldHandleUsernameNotFound() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found");
        ResponseEntity<Map<String, String>> response = handler.handleAuthentication(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not found", response.getBody().get("error"));
    }

    @Test
    void shouldHandleValidationErrors() {
        BeanPropertyBindingResult br = new BeanPropertyBindingResult(new Object(), "testObject");
        br.addError(new FieldError("testObject", "firstName",
                "First name cannot be blank"));

        br.addError(new FieldError("testObject", "dateOfBirth",
                "Date of birth must be in the past"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, br);
        ResponseEntity<Map<String, String>> response = handler.handleValidation(ex);
        Map<String, String> body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", body.get("error"));
        assertEquals("First name cannot be blank", body.get("firstName"));
        assertEquals("Date of birth must be in the past", body.get("dateOfBirth"));
    }

    @Test
    void shouldHandleInvalidEnumValue() {
        InvalidFormatException cause = new InvalidFormatException(null, "Bad enum", "INVALID",
                                                                        TrainingTypeEnum.class);

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid", cause, null);
        ResponseEntity<Map<String, String>> response = handler.handleInvalidFormat(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid training type", response.getBody().get("error"));
    }

    @Test
    void shouldHandleInvalidJsonFormat() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Bad JSON", null);
        ResponseEntity<Map<String, String>> response = handler.handleInvalidFormat(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid format of data", response.getBody().get("error"));
    }

    @Test
    void shouldHandleUnexpectedException() {
        Exception ex = new RuntimeException("Boom");
        assertDoesNotThrow(() -> handler.handleUnexpected(ex));
    }
}
