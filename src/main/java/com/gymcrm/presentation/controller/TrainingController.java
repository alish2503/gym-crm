package com.gymcrm.presentation.controller;

import com.gymcrm.application.service.TrainingService;
import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import com.gymcrm.presentation.dto.request.TrainingFilterDto;
import com.gymcrm.presentation.dto.response.TrainingDto;
import com.gymcrm.presentation.dto.response.TrainingTypeDto;
import com.gymcrm.presentation.mapper.TrainingDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/trainings")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<Void> addTraining(@RequestBody @Valid CreateTrainingDto request) {
        trainingService.createTraining(TrainingDtoMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/trainees/{username}")
    public List<TrainingDto> getTrainingsForTrainee(
            @PathVariable String username,
            @ModelAttribute @Valid TrainingFilterDto filterDto)
    {
        return trainingService.getTrainingsForTrainee(username, TrainingDtoMapper.toDomain(filterDto)).
                stream().map(TrainingDtoMapper::toDtoForTrainee).toList();
    }

    @GetMapping("/trainers/{username}")
    public List<TrainingDto> getTrainingsForTrainer(
            @PathVariable String username,
            @ModelAttribute @Valid TrainingFilterDto filterDto)
    {
        return trainingService.getTrainingsForTrainer(username, TrainingDtoMapper.toDomain(filterDto)).
                stream().map(TrainingDtoMapper::toDtoForTrainer).toList();
    }

    @GetMapping("/training-types")
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
