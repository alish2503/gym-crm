package com.gymcrm.exception;

/**
 * @author Alish
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
