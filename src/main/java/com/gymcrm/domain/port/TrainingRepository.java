package com.gymcrm.domain.port;

import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Alish
 */
public interface TrainingRepository extends BaseRepository<Training> {
    List<Training> getTrainings(String userName, LocalDate from, LocalDate to, String otherName, TrainingTypeEnum typeEnum);
}
