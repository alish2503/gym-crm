package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;

/**
 * @author Alish
 */
public class TrainerDto extends FullNameDto {
    private String username;
    private String specialization;

    public TrainerDto(String username, String firstName, String lastName, String specialization) {
        super(firstName, lastName);
        this.username = username;
        this.specialization = specialization;
    }
}
