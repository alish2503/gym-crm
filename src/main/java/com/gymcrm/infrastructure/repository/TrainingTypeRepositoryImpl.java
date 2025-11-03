package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.mapper.TrainingTypeMapper;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alish
 */
@Repository
class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    protected EntityManager entityManager;

    public TrainingTypeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<TrainingType> findByName(TrainingTypeEnum typeEnum) {
        return entityManager.
                createQuery("from training_types type where type.name = :typeName ", TrainingTypeDao.class).
                setParameter("typeName", typeEnum).
                getResultStream().findFirst().map(TrainingTypeMapper::toDomain);
    }
}
