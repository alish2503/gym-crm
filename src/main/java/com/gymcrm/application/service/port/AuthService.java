package com.gymcrm.application.service.port;

/**
 * @author Alish
 */
public interface AuthService {
    String login(String username, String rawPassword);
    void logout(String authHeader);
}
