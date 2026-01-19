package com.gymcrm.application.service;

/**
 * @author Alish
 */
public interface CredentialService {
    String generateUsername(String firstName, String lastName);
    String generatePassword();
}
