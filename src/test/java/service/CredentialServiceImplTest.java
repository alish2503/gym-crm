package service;

import com.gymcrm.application.service.impl.CredentialServiceImpl;
import com.gymcrm.domain.port.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */
@ExtendWith(MockitoExtension.class)
class CredentialServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CredentialServiceImpl credentialService;

    @Test
    void generateUsername_whenNotExists() {
        when(userProfileRepository.existsByUserName("John.Doe")).thenReturn(false);
        String username = credentialService.generateUsername("John", "Doe");
        assertEquals("John.Doe", username);
        verify(userProfileRepository).existsByUserName("John.Doe");
    }

    @Test
    void generateUsername_shouldAppendCounter_whenExists() {
        when(userProfileRepository.existsByUserName("John.Doe")).thenReturn(true);
        when(userProfileRepository.existsByUserName("John.Doe1")).thenReturn(false);
        String username = credentialService.generateUsername("John", "Doe");
        assertEquals("John.Doe1", username);
        verify(userProfileRepository).existsByUserName("John.Doe");
        verify(userProfileRepository).existsByUserName("John.Doe1");
    }

    @Test
    void generatePassword_shouldReturnRandomPasswordOfCorrectLength() {
        String password = credentialService.generatePassword();
        assertEquals(10, password.length());
        assertTrue(password.matches("[A-Za-z0-9]+"));
    }

    @Test
    void encodePassword_shouldDelegateToPasswordEncoder() {
        when(passwordEncoder.encode("raw")).thenReturn("encoded");
        String encoded = credentialService.encodePassword("raw");
        assertEquals("encoded", encoded);
        verify(passwordEncoder).encode("raw");
    }

    @Test
    void passwordMatches_shouldDelegateToPasswordEncoder() {
        when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);
        boolean result = credentialService.passwordMatches("raw", "encoded");
        assertTrue(result);
        verify(passwordEncoder).matches("raw", "encoded");
    }
}
