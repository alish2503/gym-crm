package com.gymcrm.application.service;

/**
 * @author Alish
 */
public interface UserService {
    void changePassword(String username, String newPassword);
    void toggle(String username);
}
