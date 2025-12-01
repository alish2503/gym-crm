package com.gymcrm.application.request;

import com.gymcrm.domain.model.FullName;
import lombok.Getter;

/**
 * @author Alish
 */
@Getter
public class UpdateUserRequest extends FullName {
    private final String username;
    private final boolean isActive;

    public UpdateUserRequest(String username, String firstName, String lastName, boolean isActive)
    {
        super(firstName, lastName);
        this.username = username;
        this.isActive = isActive;
    }

}
