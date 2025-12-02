package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.port.AuthService;
import com.gymcrm.infrastructure.security.service.port.BruteForceProtectionService;
import com.gymcrm.infrastructure.security.service.port.CustomUserDetailsService;
import com.gymcrm.infrastructure.security.service.port.JwtService;
import com.gymcrm.infrastructure.security.service.port.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Alish
 */

/**
 * @author Alish
 */

@Service
public class AuthServiceImpl implements AuthService {
    private final BruteForceProtectionService bruteForceProtectionService;
    private final TokenBlacklistService tokenBlacklistService;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public AuthServiceImpl(BruteForceProtectionService bruteForceProtectionService, TokenBlacklistService tokenBlacklistService,
                           CustomUserDetailsService userDetailsService, JwtService jwtService)
    {
        this.bruteForceProtectionService = bruteForceProtectionService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String username, String rawPassword) {
        long remaining = bruteForceProtectionService.checkBlocked(username);
        if (remaining > 0) {
            throw new LockedException("User blocked for " + remaining + " ms");
        }
        if (!userDetailsService.isValidPassword(username, rawPassword)) {
            bruteForceProtectionService.loginFailed(username);
            throw new BadCredentialsException("Wrong password");
        }
        bruteForceProtectionService.loginSucceeded(username);
        return jwtService.generateToken(username);
    }

    @Override
    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InsufficientAuthenticationException("No token provided");
        }
        String token = authHeader.substring(7);
        Date expiration = jwtService.getExpiration(token);
        tokenBlacklistService.blacklist(token, expiration);
    }
}
