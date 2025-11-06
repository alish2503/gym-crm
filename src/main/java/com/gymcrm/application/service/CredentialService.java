package com.gymcrm.application.service;

/**
 * @author Alish
 */
public interface CredentialService {
    String generateUserName(String firstName, String lastName);
    String generatePassword();
    String encodePassword(String rawPassword);
    boolean passwordMatches(String rawPassword, String encodedPassword);
}
