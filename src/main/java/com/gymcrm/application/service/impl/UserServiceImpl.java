package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateUserRequest;
import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.UserService;
import com.gymcrm.domain.model.UserProfile;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.domain.port.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/**
 * @author Alish
 */
abstract class UserServiceImpl<E extends UserProfile> implements UserService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final Class<E> userClass;
    private final CredentialService credentialService;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository<E> userRepository;

    public UserServiceImpl(UserRepository<E> userRepository, UserProfileRepository userProfileRepository,
                           CredentialService credentialService, Class<E> userClas)
    {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.credentialService = credentialService;
        this.userClass = userClas;
    }

    protected UserCredentials createUser(CreateUserRequest request, E created) {
        String userEntityName = userClass.getSimpleName();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        log.info("Creating new {}: {} {}", userEntityName.toLowerCase(), firstName, lastName);
        String username = credentialService.generateUsername(firstName, lastName);
        String password = credentialService.generatePassword();
        String hashed = credentialService.encodePassword(password);
        User userProfile = new User(username, hashed, firstName, lastName);
        created.setUser(userProfile);
        userRepository.save(created);
        log.info("{} created successfully with username: {}", userEntityName, username);
        return new UserCredentials(username, password);
    }

    @Override
    @Transactional
    public void changePassword(String username, String newPassword) {
        log.info("Changing password for user {}", username);
        String hashed = credentialService.encodePassword(newPassword);
        updateUserProfile(username, profile -> profile.setPassword(hashed));
    }

    @Override
    @Transactional
    public void toggle(String username, boolean isActive) {
        log.info("Activating user {}", username);
        updateUserProfile(username, profile -> profile.setActive(isActive));
    }

    protected void updateUserProfile(String username, Consumer<User> updater) {
        User userProfile = userProfileRepository.findProfileByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        updater.accept(userProfile);
        userProfileRepository.updateProfile(userProfile);
    }

    protected void updateUser(E user, UpdateUserRequest request) {
        User userProfile = user.getUser();
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setActive(request.isActive());
        userRepository.update(user);
    }
}

