package com.gymcrm.presentation.controller;

import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTraineeDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingForTraineeDto;
import com.gymcrm.presentation.dto.response.TrainingForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingTypeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Alish
 */
@Tag(name = "Trainings")
@RequestMapping(path = "/trainings", produces = "application/json")
public interface TrainingControllerApi {

    @Operation(summary = "Create a new training", description = "Creates a new training session for a trainee and trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or training already exists"),
            @ApiResponse(responseCode = "401", description = "Invalid token"),
            @ApiResponse(responseCode = "404", description = "Trainer or trainee or training type not found")
    })
    void addTraining(CreateTrainingDto request);

    @Operation(summary = "Get trainings for trainee", description = "Fetch list of trainings for specific trainee using filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of trainings",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainingForTraineeDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee or training type not found", content = @Content)
    })
    List<TrainingForTraineeDto> getTrainingsForTrainee(
            String username,
            @ParameterObject TrainingFilterForTraineeDto filterDto
    );

    @Operation(summary = "Get trainings for trainer", description = "Fetch list of trainings for specific trainer using filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of trainings",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainingForTrainerDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    List<TrainingForTrainerDto> getTrainingsForTrainer(
            String username,
            @ParameterObject TrainingFilterForTrainerDto filterDto
    );

    @Operation(summary = "Get all training types", description = "Returns list of available training types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of training types", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid token", content = @Content)
    })
    List<TrainingTypeDto> getTrainingTypes();
}
