package com.gymcrm.infrastructure.security.service.port;

import java.util.Date;

/**
 * @author Alish
 */
public interface JwtService {
    String generateToken(String username);
    boolean isValidToken(String token);
    String getUsername(String token);
    Date getExpiration(String token);
}
