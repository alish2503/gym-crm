package com.gymcrm.infrastructure.security.service.port;

/**
 * @author Alish
 */
public interface BruteForceProtectionService {
    void loginSucceeded(String username);
    void loginFailed(String username);
    long checkBlocked(String username);
}
