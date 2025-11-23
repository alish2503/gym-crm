package com.gymcrm.application.service.port;

/**
 * @author Alish
 */
public interface CredentialService {
    String generateUsername(String firstName, String lastName);
    String generatePassword();
}
