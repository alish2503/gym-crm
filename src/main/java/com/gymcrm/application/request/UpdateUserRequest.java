package com.gymcrm.application.request;

/**
 * @author Alish
 */
public class UpdateUserRequest extends CreateUserRequest {
    private String username;

    public UpdateUserRequest(String username, String firstName, String lastName, boolean isActive)
    {
        super(isActive, firstName, lastName);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
