package com.gymcrm.infrastructure.mapper;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;

/**
 * @author Alish
 */
public class TrainingTypeMapper {

    public static TrainingTypeDao toDao(TrainingType type) {
        return new TrainingTypeDao(type.id(), type.name());
    }

    public static TrainingType toDomain(TrainingTypeDao dao) {
        return new TrainingType(dao.getId(), dao.getName());
    }
}
