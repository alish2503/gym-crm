package com.gymcrm.infrastructure.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Alish
 */

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public JwtAuthenticationToken(String token) {
        super(token, null);
    }

    public String getToken() {
        return (String) getPrincipal();
    }
}
