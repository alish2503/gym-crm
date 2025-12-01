package infrastructure.filter;

import com.gymcrm.infrastructure.security.filter.JwtRequestFilter;
import com.gymcrm.infrastructure.security.service.impl.JwtServiceImpl;
import com.gymcrm.infrastructure.security.service.impl.TokenBlacklistServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenBlacklistServiceImpl tokenBlacklistService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtRequestFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void skipAuthEndpoints() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/auth/login");
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void noAuthorizationHeader() throws Exception {
        when(request.getRequestURI()).thenReturn("/for-authenticated");
        when(request.getHeader("Authorization")).thenReturn(null);
        filter.doFilter(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void blacklistedToken() throws Exception {
        String token = "token123";
        when(request.getRequestURI()).thenReturn("/for-authenticated");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenBlacklistService.isBlacklisted(token)).thenReturn(true);
        filter.doFilter(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void invalidToken() throws Exception {
        String token = "badtoken";
        when(request.getRequestURI()).thenReturn("/for-authenticated");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenBlacklistService.isBlacklisted(token)).thenReturn(false);
        when(jwtService.isValidToken(token)).thenReturn(false);
        filter.doFilter(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void validToken_setsAuthentication() throws Exception {
        String token = "goodtoken";
        String username = "John.Doe";
        when(request.getRequestURI()).thenReturn("/for-authenticated");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenBlacklistService.isBlacklisted(token)).thenReturn(false);
        when(jwtService.isValidToken(token)).thenReturn(true);
        when(jwtService.getUsername(token)).thenReturn(username);
        UserDetails user = new User(username, "", Collections.emptyList());
        when(userDetailsService.loadUserByUsername(username)).thenReturn(user);
        filter.doFilter(request, response, filterChain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void filterChainAlwaysCalled() throws Exception {
        when(request.getRequestURI()).thenReturn("/for-authenticated");
        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }
}

