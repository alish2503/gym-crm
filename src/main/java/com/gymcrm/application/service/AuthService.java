package com.gymcrm.application.service;

/**
 * @author Alish
 */
public interface AuthService {
    String login(String username, String rawPassword);
    void logout(String authHeader);
}
