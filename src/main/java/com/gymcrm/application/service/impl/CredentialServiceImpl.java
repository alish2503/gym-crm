package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.CredentialService;
import com.gymcrm.domain.port.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * @author Alish
 */
@Service
class CredentialServiceImpl implements CredentialService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int length = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    public CredentialServiceImpl(UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String generateUserName(String firstName, String lastName) {
        String base = firstName + "." + lastName;
        String userName = base;
        int counter = 1;
        while (userProfileRepository.existsByUserName(userName)) {
            userName = base + counter++;
        }
        return userName;
    }

    @Override
    public String generatePassword() {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
