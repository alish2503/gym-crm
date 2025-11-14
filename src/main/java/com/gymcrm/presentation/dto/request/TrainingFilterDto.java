package com.gymcrm.presentation.dto.request;

import com.gymcrm.domain.model.FullName;
import com.gymcrm.domain.model.TrainingFilter;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.presentation.dto.FullNameDto;
import com.gymcrm.presentation.validation.ValidTrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author Alish
 */
public class TrainingFilterDto {
    private LocalDate from;
    private LocalDate to;

    @Size(max = 50)
    private String personName;

    @ValidTrainingType
    private String type;

    public TrainingFilterDto() {}

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public String getPersonName() {
        return personName;
    }

    public String getType() {
        return type;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setType(String type) {
        this.type = type;
    }
}
