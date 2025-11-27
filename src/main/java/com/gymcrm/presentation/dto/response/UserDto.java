package com.gymcrm.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Alish
 */
public class UserDto extends FullNameDto {

    @Schema(example = "false")
    @JsonProperty("isActive")
    private final boolean isActive;

    public UserDto(boolean isActive, String firstName, String lastName) {
        super(firstName, lastName);
        this.isActive = isActive;
    }

    @JsonProperty("isActive")
    public boolean isActive() {
        return isActive;
    }
}
