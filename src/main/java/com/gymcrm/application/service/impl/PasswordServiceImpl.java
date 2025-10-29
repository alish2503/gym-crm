package com.gymcrm.application.service.impl;

import com.gymcrm.application.service.PasswordService;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * @author Alish
 */
@Component
public class PasswordServiceImpl implements PasswordService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}