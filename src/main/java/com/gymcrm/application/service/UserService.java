package com.gymcrm.application.service;

import com.gymcrm.application.dto.request.UserRequest;

/**
 * @author Alish
 */
public interface UserService {
    void changePassword(UserRequest request);
    void activate(String userName);
}
