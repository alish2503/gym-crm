package com.gymcrm.application.service.impl;

import com.gymcrm.domain.port.UserRepository;
import com.gymcrm.domain.exception.EntityNotFoundException;
import com.gymcrm.domain.model.User;
import com.gymcrm.application.service.PasswordService;
import com.gymcrm.application.service.UserService;

/**
 * @author Alish
 */
public abstract class UserServiceImpl<E extends User> extends BaseServiceImpl<E> implements UserService<E> {
    private final UserRepository<E> userRepository;
    private final PasswordService passwordService;

    protected UserServiceImpl(UserRepository<E> userRepository, PasswordService passwordService) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public E create(E user) {
        log.info("Creating new {}: {} {}", user.getClass().getSimpleName(),
                user.getFirstName(), user.getLastName());

        String base = user.getFirstName() + "." + user.getLastName();
        String username = base;
        int counter = 1;
        while (userRepository.findByUsername(username).isPresent()) {
            username = base + counter++;
        }
        user.setUsername(username);
        user.setPassword(passwordService.generateRandomPassword(10));
        userRepository.save(user);
        log.info("{} {} created successfully with username: {}",
                user.getClass().getSimpleName(),
                user.getFirstName(),
                username);

        return user;
    }

    @Override
    public E getByUsername(String username) {
        log.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    @Override
    public void update(E user) {
        E found = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Can't update user that doesn't exist"));

        if (!found.getPassword().equals(user.getPassword())) {
            throw new IllegalStateException("Can't update password manually");
        }
        log.info("Updating {} with username: {}", user.getClass().getSimpleName(), user.getUsername());
        userRepository.update(user);
        log.debug("User {} updated", user.getUsername());
    }
}

