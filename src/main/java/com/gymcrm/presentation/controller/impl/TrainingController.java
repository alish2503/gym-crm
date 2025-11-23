package com.gymcrm.presentation.controller.impl;

import com.gymcrm.application.service.port.TrainingService;
import com.gymcrm.presentation.controller.port.TrainingControllerApi;
import com.gymcrm.presentation.dto.request.CreateTrainingDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTraineeDto;
import com.gymcrm.presentation.dto.request.TrainingFilterForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingForTraineeDto;
import com.gymcrm.presentation.dto.response.TrainingForTrainerDto;
import com.gymcrm.presentation.dto.response.TrainingTypeDto;
import com.gymcrm.presentation.mapper.TrainingDtoMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
@RestController
public class TrainingController implements TrainingControllerApi {
    private final TrainingService trainingService;
    private static final Logger log = LoggerFactory.getLogger(TrainingController.class);

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    @PreAuthorize(
            "#request.traineeUsername == authentication.name or " +
            "#request.trainerUsername == authentication.name"
    )
    public ResponseEntity<Void> addTraining(@RequestBody @Valid CreateTrainingDto request) {
        trainingService.createTraining(TrainingDtoMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/trainees/{username}")
    @PreAuthorize("#username == authentication.name")
    public List<TrainingForTraineeDto> getTrainingsForTrainee(
            @PathVariable String username, @ModelAttribute @Valid TrainingFilterForTraineeDto filterDto
    ) {
        return trainingService.getTrainingsForTrainee(username, TrainingDtoMapper.toDomain(filterDto))
                .stream().map(TrainingDtoMapper::toDtoForTrainee).toList();
    }

    @GetMapping("/trainers/{username}")
    @PreAuthorize("#username == authentication.name")
    public List<TrainingForTrainerDto> getTrainingsForTrainer(
            @PathVariable String username, @ModelAttribute @Valid TrainingFilterForTrainerDto filterDto
    ) {
        return trainingService.getTrainingsForTrainer(username, TrainingDtoMapper.toDomain(filterDto))
                .stream().map(TrainingDtoMapper::toDtoForTrainer).toList();
    }

    @GetMapping("/training-types")
    public List<TrainingTypeDto> getTrainingTypes() {
        return trainingService.getTrainingTypes().stream().map(TrainingDtoMapper::toDto).toList();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleExistingTraining(DataIntegrityViolationException ex) {
        log.warn(ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }
}
