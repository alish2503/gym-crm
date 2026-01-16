package com.gymcrm.application.event;


import java.time.LocalDate;

public record TrainerWorkloadEvent(
        String username,
        String firstName,
        String lastName,
        Boolean isActive,
        LocalDate date,
        Integer durationInHours,
        ActionType actionType
) {}
