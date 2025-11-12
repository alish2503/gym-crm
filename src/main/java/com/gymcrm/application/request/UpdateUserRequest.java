package com.gymcrm.application.request;

/**
 * @author Alish
 */
public class UpdateUserRequest extends CreateUserRequest {
    private String username;
    private String password;

    public UpdateUserRequest(String username, String password, String firstName, String lastName,
                             boolean isActive)
    {
        super(isActive, firstName, lastName);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
