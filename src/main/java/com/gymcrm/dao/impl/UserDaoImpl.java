package com.gymcrm.dao.impl;

import com.gymcrm.dao.UserDao;
import com.gymcrm.model.User;
import com.gymcrm.storage.InMemoryStorage;

import java.util.Optional;

/**
 * @author Alish
 */
public abstract class UserDaoImpl<T extends User> extends BaseDaoImpl<T, String> implements UserDao<T> {

    protected UserDaoImpl(InMemoryStorage storage, String namespace) {
        super(storage, namespace);
    }

    @Override
    public Optional<T> findByUsername(String username) {
        return Optional.ofNullable(storageMap.get(username));
    }

    @Override
    public T save(T user) {
        storageMap.put(user.getUsername(), user);
        return user;
    }

    @Override
    public void update(T user) {
        storageMap.put(user.getUsername(), user);
    }
}
