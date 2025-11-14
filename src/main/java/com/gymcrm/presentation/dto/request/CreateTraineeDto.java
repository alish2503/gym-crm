package com.gymcrm.presentation.dto.request;

import com.gymcrm.presentation.dto.FullNameDto;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class CreateTraineeDto extends FullNameDto {

    @Past
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "")
    private String address;

    public CreateTraineeDto(String firstName, String lastName, LocalDate dateOfBirth,
                            String address)
    {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public CreateTraineeDto() {}

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
