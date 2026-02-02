package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.AuthService;
import com.gymcrm.infrastructure.security.service.BruteForceProtectionService;
import com.gymcrm.infrastructure.security.service.CustomUserDetailsService;
import com.gymcrm.infrastructure.security.service.JwtService;
import com.gymcrm.infrastructure.security.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Alish
 */

@Service
@Profile("security")
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
        return jwtService.generateTokenForUser(username);
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
