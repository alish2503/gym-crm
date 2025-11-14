package com.gymcrm.presentation.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class UpdateTraineeDto extends UpdateUserDto {

    @Past
    private LocalDate dateOfBirth;

    @Size(max = 255)
    private String address;

    public UpdateTraineeDto(String firstName, String lastName, boolean isActive,
                            LocalDate dateOfBirth, String address)
    {
        super(firstName, lastName, isActive);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public UpdateTraineeDto() {}

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
