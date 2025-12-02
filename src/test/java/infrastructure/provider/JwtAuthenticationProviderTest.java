package infrastructure.provider;


import com.gymcrm.infrastructure.security.provider.JwtAuthenticationProvider;
import com.gymcrm.infrastructure.security.service.port.JwtService;
import com.gymcrm.infrastructure.security.service.port.TokenBlacklistService;
import com.gymcrm.infrastructure.security.token.JwtAuthenticationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationProviderTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private JwtAuthenticationProvider provider;

    @Test
    void shouldAuthenticateValidToken() {
        JwtAuthenticationToken auth = new JwtAuthenticationToken("VALID");
        when(tokenBlacklistService.isBlacklisted("VALID")).thenReturn(false);
        when(jwtService.isValidToken("VALID")).thenReturn(true);
        when(jwtService.getUsername("VALID")).thenReturn("john.doe");
        Authentication result = provider.authenticate(auth);
        assertNotNull(result);
        assertEquals("john.doe", result.getPrincipal());
        assertNull(result.getCredentials());
        assertTrue(result.getAuthorities().isEmpty());
        verify(tokenBlacklistService).isBlacklisted("VALID");
        verify(jwtService).isValidToken("VALID");
        verify(jwtService).getUsername("VALID");
    }

    @Test
    void shouldThrowIfTokenInvalidOrBlacklisted() {
        JwtAuthenticationToken auth = new JwtAuthenticationToken("BAD");
        when(tokenBlacklistService.isBlacklisted("BAD")).thenReturn(true);
        assertThrows(BadCredentialsException.class, () -> provider.authenticate(auth));
        verify(jwtService, never()).getUsername(any());
    }

    @Test
    void supportsJwtAuthenticationToken() {
        assertTrue(provider.supports(JwtAuthenticationToken.class));
        assertFalse(provider.supports(Authentication.class));
    }
}
