package com.gymcrm.service.impl;

import com.gymcrm.dao.UserDao;
import com.gymcrm.model.User;
import com.gymcrm.service.UserService;

/**
 * @author Alish
 */
public abstract class UserServiceImpl<T extends User> extends BaseServiceImpl<T, String> implements UserService<T> {

    protected final UserDao<T> userDao;

    protected UserServiceImpl(UserDao<T> userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    @Override
    public T create(T user) {
        String base = user.getFirstName() + "." + user.getLastName();
        String username = base;
        int counter = 1;
        while (userDao.findByUsername(username) != null) {
            username = base + counter++;
        }
        user.setUsername(username);
        user.setPassword(PasswordGenerator.generateRandomPassword(10));
        userDao.save(user);
        return user;
    }

    @Override
    public T getByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void update(T user) {
        userDao.update(user);
    }
}

