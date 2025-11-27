package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Alish
 */
public class TraineeDto extends FullNameDto {

    @Schema(example = "John.Doe")
    private final String username;

    public TraineeDto(String username, String firstName, String lastName) {
        super(firstName, lastName);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
