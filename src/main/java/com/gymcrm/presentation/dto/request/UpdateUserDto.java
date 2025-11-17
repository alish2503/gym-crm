package com.gymcrm.presentation.dto.request;

import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author Alish
 */
public class UpdateUserDto extends FullNameDto {

    @Schema(example = "false")
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
