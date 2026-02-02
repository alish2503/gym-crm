package com.gymcrm.domain.model;

import java.time.LocalDate;

/**
 * @author Alish
 */
public record TrainingFilter(LocalDate from, LocalDate to, FullName personName, TrainingTypeEnum type) {}
