package com.gymcrm.application.request;

import com.gymcrm.domain.model.FullName;

/**
 * @author Alish
 */
public class UpdateUserRequest extends FullName {
    private final String username;
    private final boolean isActive;

    public UpdateUserRequest(String username, String firstName, String lastName, boolean isActive)
    {
        super(firstName, lastName);
        this.username = username;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public boolean isActive() {
        return isActive;
    }
}
