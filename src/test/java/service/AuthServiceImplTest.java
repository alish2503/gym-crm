package service;

import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.impl.AuthServiceImpl;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private CredentialService credentialService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authenticate_success() {
        User user = new User();
        user.setPassword("encoded");
        when(userProfileRepository.findProfileByUserName("john")).thenReturn(Optional.of(user));
        when(credentialService.passwordMatches("raw", "encoded")).thenReturn(true);
        User result = authService.authenticate("john", "raw");
        assertNotNull(result);
        assertEquals(user, result);
        verify(userProfileRepository).findProfileByUserName("john");
        verify(credentialService).passwordMatches("raw", "encoded");
        verifyNoMoreInteractions(userProfileRepository, credentialService);
    }

    @Test
    void authenticate_userNotFound_throwsException() {
        when(userProfileRepository.findProfileByUserName("john")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                authService.authenticate("john", "raw")
        );
        verify(userProfileRepository).findProfileByUserName("john");
        verifyNoMoreInteractions(userProfileRepository, credentialService);
    }

    @Test
    void authenticate_incorrectPassword_throwsSecurityException() {
        User user = new User();
        user.setPassword("encoded");
        when(userProfileRepository.findProfileByUserName("john")).thenReturn(Optional.of(user));
        when(credentialService.passwordMatches("raw", "encoded")).thenReturn(false);
        assertThrows(SecurityException.class, () ->
                authService.authenticate("john", "raw")
        );
        verify(userProfileRepository).findProfileByUserName("john");
        verify(credentialService).passwordMatches("raw", "encoded");
        verifyNoMoreInteractions(userProfileRepository, credentialService);
    }
}
