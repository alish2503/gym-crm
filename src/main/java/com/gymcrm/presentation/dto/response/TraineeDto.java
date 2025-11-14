package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;

/**
 * @author Alish
 */
public class TraineeDto extends FullNameDto {
    private String username;

    public TraineeDto(String username, String firstName, String lastName) {
        super(firstName, lastName);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
