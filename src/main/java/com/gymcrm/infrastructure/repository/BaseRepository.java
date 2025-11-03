package com.gymcrm.infrastructure.repository;

import jakarta.persistence.EntityManager;

/**
 * @author Alish
 */
abstract class BaseRepository<E, D> {
    protected EntityManager entityManager;

    public BaseRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(E entity) {
        entityManager.persist(mapToDao(entity));
    }

    protected abstract D mapToDao(E entity);
}
