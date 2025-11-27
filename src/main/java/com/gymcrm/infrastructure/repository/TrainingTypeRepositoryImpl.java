package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.mapper.TrainingTypeDaoMapper;
import com.gymcrm.infrastructure.dao.TrainingTypeDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingType> findByName(TrainingTypeEnum typeEnum) {
        return entityManager.
                createQuery("from TrainingTypeDao where name = :typeName ", TrainingTypeDao.class).
                setParameter("typeName", typeEnum).
                getResultStream().findFirst().map(TrainingTypeDaoMapper::toDomain);
    }

    @Override
    public List<TrainingType> findAll() {
        String jpql = "from TrainingTypeDao";
        return entityManager.createQuery(jpql, TrainingTypeDao.class).
                getResultList().stream().map(TrainingTypeDaoMapper::toDomain).toList();
    }

    @Override
    public boolean existsByName(TrainingTypeEnum typeEnum) {
        String jpql = "select count(t) from TrainingTypeDao t where t.name = :typeName";
        Long count = entityManager.createQuery(jpql, Long.class).setParameter("typeName", typeEnum).getSingleResult();
        return count > 0;
    }

}
