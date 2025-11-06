package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.AuthService;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.UserService;
import com.gymcrm.domain.model.HasUserProfile;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.domain.port.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/**
 * @author Alish
 */
abstract class UserServiceImpl<E extends HasUserProfile> implements UserService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final CredentialService credentialService;
    protected final AuthService authService;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository<E> userRepository;

    public UserServiceImpl(UserRepository<E> userRepository, UserProfileRepository userProfileRepository,
                           CredentialService credentialService,
                           AuthService authService)
    {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.credentialService = credentialService;
        this.authService = authService;
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        log.info("Changing password for user {}", username);
        String hashed = credentialService.encodePassword(newPassword);
        updateUserProfile(username, oldPassword, profile -> profile.setPassword(hashed));
    }

    @Override
    public void activate(String username, String password) {
        log.info("Activating user {}", username);
        updateUserProfile(username, password, profile -> profile.setActive(true));
    }

    @Override
    public void deactivate(String username, String password) {
        log.info("Deactivating user {}", username);
        updateUserProfile(username, password, profile -> profile.setActive(false));
    }

    @Transactional
    protected void updateUserProfile(String username, String password, Consumer<User> updater) {
        User userProfile = authService.authenticate(username, password);
        updater.accept(userProfile);
        userProfileRepository.updateProfile(userProfile);
    }

    protected void setCredentials(User userProfile) {
        String firstName = userProfile.getFirstName();
        String lastName = userProfile.getLastName();
        String userName = credentialService.generateUserName(firstName, lastName);
        String password = credentialService.generatePassword();
        String hashed = credentialService.encodePassword(password);
        userProfile.setUsername(userName);
        userProfile.setPassword(hashed);
    }

    protected void updateFullNameAndSave(E user, String firstName, String lastName) {
        User userProfile = user.getUserProfile();
        userProfile.setFirstName(firstName);
        userProfile.setLastName(lastName);
        userRepository.update(user);
    }
}

