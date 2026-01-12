package com.gymcrm.infrastructure.service;

import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.infrastructure.security.details.CustomUserDetails;
import com.gymcrm.infrastructure.security.service.impl.CustomUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Alish
 */

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private CustomUserDetailsServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test", "hashedPassword");
    }

    @Test
    void loadUserByUsername_userExists_returnsCustomUserDetails() {
        when(userProfileRepository.findProfileByUsername("test")).thenReturn(Optional.of(user));
        UserDetails result = service.loadUserByUsername("test");
        assertNotNull(result);
        assertEquals("test", result.getUsername());
        assertInstanceOf(CustomUserDetails.class, result);
    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {
        when(userProfileRepository.findProfileByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("unknown"));
    }

    @Test
    void isValidPassword_correctPassword_returnsTrue() {
        when(userProfileRepository.findProfileByUsername("test")).thenReturn(Optional.of(user));
        when(encoder.matches("rawPassword", "hashedPassword")).thenReturn(true);
        boolean result = service.isValidPassword("test", "rawPassword");
        assertTrue(result);
    }

    @Test
    void isValidPassword_incorrectPassword_returnsFalse() {
        when(userProfileRepository.findProfileByUsername("test")).thenReturn(Optional.of(user));
        when(encoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);
        boolean result = service.isValidPassword("test", "wrongPassword");
        assertFalse(result);
    }
}
