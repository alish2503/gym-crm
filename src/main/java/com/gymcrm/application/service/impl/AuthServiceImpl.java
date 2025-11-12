package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.AuthService;
import com.gymcrm.application.service.CredentialService;
import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UserProfileRepository userProfileRepository;
    private final CredentialService credentialService;

    @Autowired
    public AuthServiceImpl(UserProfileRepository userProfileRepository, CredentialService credentialService) {
        this.userProfileRepository = userProfileRepository;
        this.credentialService = credentialService;
    }

    public User authenticate(String username, String rawPassword) {
        User user = userProfileRepository.findProfileByUserName(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));

        if (!credentialService.passwordMatches(rawPassword, user.getPassword())) {
            throw new SecurityException("Incorrect password");
        }
        return user;
    }
}
