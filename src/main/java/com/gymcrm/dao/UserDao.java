package com.gymcrm.dao;

import com.gymcrm.model.User;

import java.util.Optional;

/**
 * @author Alish
 */
public interface UserDao<T extends User> extends BaseDao<T, String> {
    Optional<T> findByUsername(String username);
    void update(T user);
}
