package com.gymcrm.application.service;

/**
 * @author Alish
 */
public interface UserService {
    void changePassword(String username, String oldPassword, String newPassword);
    void activate(String username, String password);
    void deactivate(String username, String password);
}
