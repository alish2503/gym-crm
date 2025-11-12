package com.gymcrm.application.request;

import com.gymcrm.domain.model.FullName;

/**
 * @author Alish
 */
public class CreateUserRequest extends FullName {
    private boolean isActive;

    public CreateUserRequest(boolean isActive, String firstName, String lastName) {
        super(firstName, lastName);
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }
}
