package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Alish
 */
public class TrainerDto extends FullNameDto {

    @Schema(example = "John.Doe")
    private final String username;

    @Schema(example = "FITNESS")
    private final String specialization;

    public TrainerDto(String username, String firstName, String lastName, String specialization) {
        super(firstName, lastName);
        this.username = username;
        this.specialization = specialization;
    }

    public String getUsername() {
        return username;
    }

    public String getSpecialization() {
        return specialization;
    }
}
