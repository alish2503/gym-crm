package com.gymcrm.infrastructure.security.service.port;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Alish
 */
public interface CustomUserDetailsService extends UserDetailsService {
    boolean isValidPassword(String username, String rawPassword);
}
