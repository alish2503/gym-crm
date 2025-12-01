package com.gymcrm.presentation.dto.request;

import com.gymcrm.domain.model.TrainingTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alish
 */
@Setter
@Getter
@NoArgsConstructor
public class TrainingFilterForTraineeDto extends TrainingFilterDto {

    @Schema(description= "Example: John Doe")
    @Size(max = 50)
    private String trainerName;

    private TrainingTypeEnum type;
}
