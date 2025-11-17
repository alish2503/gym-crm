package com.gymcrm.presentation.controller;

import com.gymcrm.application.service.TrainingService;
import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTraineeDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingForTraineeDto;
import com.gymcrm.presentation.dto.response.TrainingForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingTypeDto;
import com.gymcrm.presentation.mapper.TrainingDtoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
@RestController
@RequestMapping(path = "/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainings", description = "Endpoints for managing trainings")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    @Operation(summary = "Create a new training", description = "Creates a new training session for a trainee and trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Training created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data or training already exists"),
            @ApiResponse(responseCode = "404", description = "Trainer or trainee or training type not found")
    })
    public ResponseEntity<Void> addTraining(@RequestBody @Valid CreateTrainingDto request) {
        trainingService.createTraining(TrainingDtoMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/trainees/{username}")
    @Operation(summary = "Get trainings for trainee", description = "Fetch list of trainings for specific trainee using filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of trainings",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainingForTraineeDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainee or training type not found", content = @Content)
    })
    public List<TrainingForTraineeDto> getTrainingsForTrainee(
            @PathVariable String username,
            @ParameterObject @ModelAttribute @Valid TrainingFilterForTraineeDto filterDto
    ) {
        return trainingService.getTrainingsForTrainee(username, TrainingDtoMapper.toDomain(filterDto))
                .stream().map(TrainingDtoMapper::toDtoForTrainee).toList();
    }

    @GetMapping("/trainers/{username}")
    @Operation(summary = "Get trainings for trainer", description = "Fetch list of trainings for specific trainer using filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of trainings",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TrainingForTrainerDto.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Trainer not found", content = @Content)
    })
    public List<TrainingForTrainerDto> getTrainingsForTrainer(
            @PathVariable String username,
            @ParameterObject @ModelAttribute @Valid TrainingFilterForTrainerDto filterDto
    ) {
        return trainingService.getTrainingsForTrainer(username, TrainingDtoMapper.toDomain(filterDto))
                .stream().map(TrainingDtoMapper::toDtoForTrainer).toList();
    }

    @GetMapping("/training-types")
    @Operation(summary = "Get all training types", description = "Returns list of available training types")
    @ApiResponse(responseCode = "200", description = "List of training types", content = @Content)
    public List<TrainingTypeDto> getTrainingTypes() {
        return trainingService.getTrainingTypes().stream().map(TrainingDtoMapper::toDto).toList();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = Map.of(
                "error", "Illegal argument",
                "message", ex.getMessage() != null ? ex.getMessage() : ""
        );
        return ResponseEntity.badRequest().body(response);
    }
}
