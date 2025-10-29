package com.gymcrm.domain.exception;

/**
 * @author Alish
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
