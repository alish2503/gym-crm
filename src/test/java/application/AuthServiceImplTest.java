package application;

import com.gymcrm.application.service.impl.AuthServiceImpl;
import com.gymcrm.infrastructure.security.service.port.CustomUserDetailsService;
import com.gymcrm.infrastructure.security.service.port.JwtService;
import com.gymcrm.infrastructure.security.service.port.LoginAttemptService;
import com.gymcrm.infrastructure.security.service.port.TokenBlacklistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_userIsBlocked_shouldThrowLockedException() {
        when(loginAttemptService.checkBlocked("user")).thenReturn(5000L);
        assertThrows(LockedException.class, () -> authService.login("user", "pass"));
        verify(userDetailsService, never()).isValidPassword(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_wrongPassword_shouldThrowBadCredentials() {
        when(loginAttemptService.checkBlocked("user")).thenReturn(0L);
        when(userDetailsService.isValidPassword("user", "pass")).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> authService.login("user", "pass"));
        verify(loginAttemptService).loginFailed("user");
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void login_success_shouldReturnToken() {
        when(loginAttemptService.checkBlocked("user")).thenReturn(0L);
        when(userDetailsService.isValidPassword("user", "pass")).thenReturn(true);
        when(jwtService.generateToken("user")).thenReturn("TOKEN123");
        String result = authService.login("user", "pass");
        assertEquals("TOKEN123", result);
        verify(loginAttemptService).loginSucceeded("user");
        verify(jwtService).generateToken("user");
    }

    @Test
    void logout_noAuthHeader_shouldThrowInsufficientAuthentication() {
        assertThrows(InsufficientAuthenticationException.class, () -> authService.logout(null));
    }

    @Test
    void logout_notBearer_shouldThrowInsufficientAuthentication() {
        assertThrows(InsufficientAuthenticationException.class, () -> authService.logout("Token ABC"));
    }

    @Test
    void logout_invalidToken_shouldThrowBadCredentials() {
        when(jwtService.isValidToken("ABC")).thenReturn(false);
        assertThrows(BadCredentialsException.class, () -> authService.logout("Bearer ABC"));
        verify(tokenBlacklistService, never()).blacklist(any(), any());
    }

    @Test
    void logout_validToken_shouldBlacklist() {
        when(jwtService.isValidToken("ABC")).thenReturn(true);
        Date exp = new Date();
        when(jwtService.getExpiration("ABC")).thenReturn(exp);
        authService.logout("Bearer ABC");
        verify(tokenBlacklistService).blacklist("ABC", exp);
    }
}
