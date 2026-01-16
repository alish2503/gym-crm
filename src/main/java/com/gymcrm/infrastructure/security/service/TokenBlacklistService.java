package com.gymcrm.infrastructure.security.service;

import java.util.Date;

/**
 * @author Alish
 */
public interface TokenBlacklistService {
    void blacklist(String token, Date expiration);
    boolean isBlacklisted(String token);
}
