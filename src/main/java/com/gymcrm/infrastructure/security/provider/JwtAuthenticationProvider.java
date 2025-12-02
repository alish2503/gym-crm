package com.gymcrm.infrastructure.security.provider;

import com.gymcrm.infrastructure.security.service.port.JwtService;
import com.gymcrm.infrastructure.security.service.port.TokenBlacklistService;
import com.gymcrm.infrastructure.security.token.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Alish
 */

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public JwtAuthenticationProvider(JwtService jwtService, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = (String) authentication.getPrincipal();
        if (tokenBlacklistService.isBlacklisted(token) || !jwtService.isValidToken(token)) {
            throw new BadCredentialsException("Invalid token");
        }
        String username = jwtService.getUsername(token);
        return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

