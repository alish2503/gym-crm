package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.port.UserService;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * @author Alish
 */
@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder encoder;
    protected final UserProfileRepository userProfileRepository;

    @Autowired
    public UserServiceImpl(UserProfileRepository userProfileRepository, PasswordEncoder encoder)
    {
        this.userProfileRepository = userProfileRepository;
        this.encoder = encoder;
    }

    @Override
    public void changePassword(String username, String newPassword) {
        String hashed = encoder.encode(newPassword);
        updateUserProfile(username, profile -> profile.setPassword(hashed));
    }

    @Override
    public void toggle(String username) {
        updateUserProfile(username, profile -> profile.setActive(!profile.isActive()));
    }

    protected void updateUserProfile(String username, Consumer<User> updater) {
        User userProfile = userProfileRepository.findProfileByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        updater.accept(userProfile);
        userProfileRepository.updateProfile(userProfile);
    }
}

