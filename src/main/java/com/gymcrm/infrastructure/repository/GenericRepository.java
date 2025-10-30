package com.gymcrm.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.Optional;

/**
 * @author Alish
 */
abstract class GenericRepository<E, D> {
    protected EntityManager entityManager;

    protected GenericRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    protected Optional<E> getSingleResultOrEmpty(Query query) {
        try {
            D userDao = (D) query.getSingleResult();
            return Optional.ofNullable(mapToDomain(userDao));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    protected abstract E mapToDomain(D dao);
}
