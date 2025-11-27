package com.gymcrm.application.service.impl;

import com.gymcrm.application.request.UpdateUserRequest;
import com.gymcrm.application.response.UserCredentials;
import com.gymcrm.application.service.port.CredentialService;
import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.UserProfile;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.domain.port.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Alish
 */

abstract class AbstractUserService<E extends UserProfile> {
    private final CredentialService credentialService;
    private final PasswordEncoder encoder;
    protected final UserProfileRepository userProfileRepository;
    private final UserRepository<E> userRepository;

    protected AbstractUserService(UserRepository<E> userRepository,
                                  UserProfileRepository userProfileRepository,
                                  PasswordEncoder encoder, CredentialService credentialService)
    {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.encoder = encoder;
        this.credentialService = credentialService;
    }

    protected UserCredentials createUser(FullName request, E created) {
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String username = credentialService.generateUsername(firstName, lastName);
        String password = credentialService.generatePassword();
        String hashed = encoder.encode(password);
        User userProfile = new User(username, hashed, firstName, lastName);
        created.setUser(userProfile);
        userRepository.save(created);
        return new UserCredentials(username, password);
    }

    protected void updateUser(E user, UpdateUserRequest request) {
        User userProfile = user.getUser();
        userProfile.setFirstName(request.getFirstName());
        userProfile.setLastName(request.getLastName());
        userProfile.setActive(request.isActive());
        userRepository.update(user);
    }
}

