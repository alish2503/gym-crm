package com.gymcrm.application.service;

import com.gymcrm.domain.model.User;

/**
 * @author Alish
 */
public interface UserService<T extends User> extends BaseService<T> {
    T getByUsername(String username);
    void update(T user);
}
