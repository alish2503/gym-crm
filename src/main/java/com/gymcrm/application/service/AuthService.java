package com.gymcrm.application.service;

import com.gymcrm.domain.model.User;

/**
 * @author Alish
 */
public interface AuthService {
    User authenticate(String username, String rawPassword);
}
