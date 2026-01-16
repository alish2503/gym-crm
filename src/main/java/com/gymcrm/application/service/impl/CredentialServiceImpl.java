package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.CredentialService;
import com.gymcrm.domain.port.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * @author Alish
 */
@Service
public class CredentialServiceImpl implements CredentialService {
    private final UserProfileRepository userProfileRepository;
    private static final int length = 10;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Autowired
    public CredentialServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public String generateUsername(String firstName, String lastName) {
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
}
