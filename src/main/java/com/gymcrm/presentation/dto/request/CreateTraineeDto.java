package com.gymcrm.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gymcrm.presentation.dto.FullNameDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class CreateTraineeDto extends FullNameDto {

    @Schema(example = "1998-03-15")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Schema(example = "\"123 Main St, London\"")
    @Size(max = 255)
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
