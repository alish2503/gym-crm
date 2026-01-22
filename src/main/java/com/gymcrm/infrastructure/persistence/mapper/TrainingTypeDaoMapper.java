package com.gymcrm.infrastructure.persistence.mapper;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainingTypeDaoMapper {

    private TrainingTypeDaoMapper() {}

    public static TrainingTypeDao toDao(TrainingType type) {
        return new TrainingTypeDao(type.id(), type.typeEnum());
    }

    public static TrainingType toDomain(TrainingTypeDao dao) {
        return new TrainingType(dao.getId(), dao.getName());
    }
}
