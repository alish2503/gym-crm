package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.port.BaseRepository;
import jakarta.persistence.EntityManager;

/**
 * @author Alish
 */
abstract class BaseRepositoryImpl<E, D> extends GenericRepository<E, D>  implements BaseRepository<E> {

    protected BaseRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public E save(E entity) {
        D dao = mapToDao(entity);
        entityManager.persist(dao);
        return mapToDomain(dao);
    }

    protected abstract D mapToDao(E entity);
}
