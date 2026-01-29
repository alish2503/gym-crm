package com.gymcrm.unit.infrastructure.filter;

import com.gymcrm.infrastructure.security.filter.JwtRequestFilter;
import com.gymcrm.infrastructure.security.token.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    FilterChain filterChain;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    Authentication authentication;

    @InjectMocks
    JwtRequestFilter jwtRequestFilter;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void shouldAuthenticateValidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer VALID_TOKEN");
        when(authenticationManager.authenticate(any(JwtAuthenticationToken.class))).thenReturn(authentication);
        jwtRequestFilter.doFilter(request, response, filterChain);
        verify(authenticationManager).authenticate(any(JwtAuthenticationToken.class));
        verify(filterChain).doFilter(request, response);
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldReturn401ForInvalidToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer INVALID");
        when(authenticationManager.authenticate(any(JwtAuthenticationToken.class))).thenThrow(
                new BadCredentialsException("Invalid token")
        );
        PrintWriter writer = new PrintWriter(new StringWriter());
        when(response.getWriter()).thenReturn(writer);
        jwtRequestFilter.doFilter(request, response, filterChain);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }


    @Test
    void shouldSkipAuthenticationWhenNoHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        jwtRequestFilter.doFilter(request, response, filterChain);
        verify(authenticationManager, never()).authenticate(any());
        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}


