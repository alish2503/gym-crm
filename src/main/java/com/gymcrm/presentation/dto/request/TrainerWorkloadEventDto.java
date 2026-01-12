package com.gymcrm.presentation.dto.request;

import java.time.LocalDate;

public record TrainerWorkloadEventDto(
        String username,
        String firstName,
        String lastName,
        boolean active,
        LocalDate date,
        int duration,
        ActionType actionType
) {}
