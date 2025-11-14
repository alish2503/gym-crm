package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;

/**
 * @author Alish
 */
public class UserDto extends FullNameDto {
    private boolean isActive;

    public UserDto(boolean isActive, String firstName, String lastName) {
        super(firstName, lastName);
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }
}
