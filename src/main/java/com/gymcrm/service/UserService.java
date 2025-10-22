package com.gymcrm.service;

import com.gymcrm.model.User;

/**
 * @author Alish
 */
public interface UserService<T extends User> extends BaseService<T, String> {
    T getByUsername(String username);
    void update(T user);
}
