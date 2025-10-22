package com.gymcrm.service.impl;

import com.gymcrm.dao.UserDao;
import com.gymcrm.exception.EntityNotFoundException;
import com.gymcrm.model.User;
import com.gymcrm.service.UserService;
import com.gymcrm.util.PasswordGenerator;

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
        log.info("Creating new {}: {} {}", user.getClass().getSimpleName(),
                user.getFirstName(), user.getLastName());

        String base = user.getFirstName() + "." + user.getLastName();
        String username = base;
        int counter = 1;
        while (userDao.findByUsername(username).isPresent()) {
            username = base + counter++;
        }
        user.setUsername(username);
        user.setPassword(PasswordGenerator.generateRandomPassword(10));
        userDao.save(user);
        log.info("{} {} created successfully with username: {}",
                user.getClass().getSimpleName(),
                user.getFirstName(),
                user.getUsername());

        return user;
    }

    @Override
    public T getByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        return userDao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    @Override
    public void update(T user) {
        log.info("Updating {} with username: {}", user.getClass().getSimpleName(), user.getUsername());
        if (userDao.findByUsername(user.getUsername()).isEmpty()) {
            throw new EntityNotFoundException("User not found: " + user.getUsername());
        }
        userDao.update(user);
        log.debug("User {} updated", user.getUsername());
    }
}

