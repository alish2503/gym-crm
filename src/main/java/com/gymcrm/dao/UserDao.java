package com.gymcrm.dao;

import com.gymcrm.model.User;

/**
 * @author Alish
 */
public interface UserDao<T extends User> extends BaseDao<T, String> {
    T findByUsername(String username);
    void update(T user);
}
