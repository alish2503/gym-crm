package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.port.BaseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author Alish
 */
abstract class BaseRepositoryImpl<E, D> implements BaseRepository<E> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public void save(E entity) {
        entityManager.persist(mapToDao(entity));
    }

    protected abstract D mapToDao(E entity);
}
