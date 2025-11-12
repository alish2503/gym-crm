package com.gymcrm.application.service;

import com.gymcrm.application.UserCredentials;

/**
 * @author Alish
 */
public interface UserService {
    void changePassword(UserCredentials credentials, String newPassword);
    void toggle(UserCredentials credentials);
}
