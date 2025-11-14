package com.gymcrm.presentation.dto.response;

import com.gymcrm.presentation.dto.FullNameDto;

import java.time.LocalDate;

/**
 * @author Alish
 */
public record TrainingDto(String trainingName, LocalDate date, String type, int duration,
                          FullNameDto personName) {}
