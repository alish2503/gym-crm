package com.gymcrm.presentation.dto.request;

import com.gymcrm.presentation.dto.FullNameDto;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alish
 */
public class UpdateUserDto extends FullNameDto {

    @NotNull(message = "Activity cannot be null")
    protected Boolean isActive;

    public UpdateUserDto(String firstName, String lastName, boolean isActive) {
        super(firstName, lastName);
        this.isActive = isActive;
    }

    public UpdateUserDto() {}

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
