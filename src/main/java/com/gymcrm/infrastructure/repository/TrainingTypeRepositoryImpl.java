package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alish
 */
@Repository
class TrainingTypeRepositoryImpl extends GenericRepository<TrainingType, TrainingTypeDao> implements TrainingTypeRepository {

    @Autowired
    public TrainingTypeRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<TrainingType> findByName(TrainingTypeEnum typeEnum) {
        TypedQuery<TrainingTypeDao> query = entityManager.
                createQuery("from training_types type where type.name = :typeName ", TrainingTypeDao.class).
                setParameter("typeName", typeEnum);
        return getSingleResultOrEmpty(query);
    }

    @Override
    protected TrainingType mapToDomain(TrainingTypeDao dao) {
        return new TrainingType(dao.getName());
    }
}
