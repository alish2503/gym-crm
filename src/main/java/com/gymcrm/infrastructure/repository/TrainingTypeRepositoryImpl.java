package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.mapper.TrainingTypeMapper;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alish
 */
@Repository
class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingType> findByName(TrainingTypeEnum typeEnum) {
        return entityManager.
                createQuery("from training_types type where type.name = :typeName ", TrainingTypeDao.class).
                setParameter("typeName", typeEnum).
                getResultStream().findFirst().map(TrainingTypeMapper::toDomain);
    }

    @Override
    public boolean existsByName(TrainingTypeEnum typeEnum) {
        String jpql = "select count(t) from TrainingTypeDao t where t.name = :typeName";
        Long count = entityManager.createQuery(jpql, Long.class).setParameter("typeName", typeEnum).getSingleResult();
        return count > 0;
    }

}
