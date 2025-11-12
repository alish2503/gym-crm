package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.CreateUserRequest;
import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.application.UserCredentials;
import com.gymcrm.application.service.AuthService;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.UserService;
import com.gymcrm.domain.model.UserProfile;
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
abstract class UserServiceImpl<E extends UserProfile> implements UserService {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final Class<E> userClass;
    private final CredentialService credentialService;
    protected final AuthService authService;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository<E> userRepository;

    public UserServiceImpl(UserRepository<E> userRepository, UserProfileRepository userProfileRepository,
                           CredentialService credentialService, AuthService authService,
                           Class<E> userClas)
    {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.credentialService = credentialService;
        this.authService = authService;
        this.userClass = userClas;
    }

    protected UserCredentials createUser(CreateUserRequest request, E created) {
        String userEntityName = userClass.getSimpleName();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        log.info("Creating new {}: {} {}", userEntityName.toLowerCase(), firstName, lastName);
        boolean isActive = request.isActive();
        String username = credentialService.generateUsername(firstName, lastName);
        String password = credentialService.generatePassword();
        String hashed = credentialService.encodePassword(password);
        User userProfile = new User(username, hashed, firstName, lastName, isActive);
        created.setUser(userProfile);
        userRepository.save(created);
        log.info("{} created successfully with username: {}", userEntityName, username);
        return new UserCredentials(username, password);
    }

    @Override
    @Transactional
    public void changePassword(UserCredentials credentials, String newPassword) {
        String username = credentials.username();
        String oldPassword = credentials.password();
        log.info("Changing password for user {}", username);
        String hashed = credentialService.encodePassword(newPassword);
        updateUserProfile(username, oldPassword, profile -> profile.setPassword(hashed));
    }

    @Override
    @Transactional
    public void toggle(UserCredentials credentials) {
        String username = credentials.username();
        String password = credentials.password();
        log.info("Activating user {}", username);
        updateUserProfile(username, password, profile -> profile.setActive(!profile.isActive()));
    }

    protected void updateUserProfile(String username, String password, Consumer<User> updater) {
        User userProfile = authService.authenticate(username, password);
        updater.accept(userProfile);
        userProfileRepository.updateProfile(userProfile);
    }

    protected void updateUser(E user, User userProfile, UpdateUserRequest request) {
        String newUsername = request.getUsername();
        if (!userProfile.getUsername().equals(newUsername) &&
            userProfileRepository.existsByUserName(newUsername))
        {
            throw new IllegalArgumentException("User with username " + newUsername + " already exists");
        }
        String hashed = credentialService.encodePassword(request.getPassword());
        userProfile.setUsername(newUsername);
        userProfile.setPassword(hashed);
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setActive(request.isActive());
        user.setUser(userProfile);
        userRepository.update(user);
    }
}

