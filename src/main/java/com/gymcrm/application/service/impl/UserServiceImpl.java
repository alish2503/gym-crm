package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.application.service.UserService;
import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.UserProfile;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.domain.port.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/**
 * @author Alish
 */
abstract class UserServiceImpl<E extends UserProfile> implements UserService {
    private final CredentialService credentialService;
    protected final UserProfileRepository userProfileRepository;
    private final UserRepository<E> userRepository;

    public UserServiceImpl(UserRepository<E> userRepository, UserProfileRepository userProfileRepository,
                           CredentialService credentialService)
    {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.credentialService = credentialService;
    }

    protected UserCredentials createUser(FullName request, E created) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String username = credentialService.generateUsername(firstName, lastName);
        String password = credentialService.generatePassword();
        String hashed = credentialService.encodePassword(password);
        User userProfile = new User(username, hashed, firstName, lastName);
        created.setUser(userProfile);
        userRepository.save(created);
        return new UserCredentials(username, password);
    }

    @Override
    @Transactional
    public void changePassword(String username, String newPassword) {
        String hashed = credentialService.encodePassword(newPassword);
        updateUserProfile(username, profile -> profile.setPassword(hashed));
    }

    @Override
    @Transactional
    public void toggle(String username, boolean isActive) {
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

