package com.gymcrm.infrastructure.adapter;

import com.gymcrm.domain.port.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Alish
 */

abstract class BaseRepositoryImpl<E, D> implements BaseRepository<E> {
    protected JpaRepository<D, Long> jpaRepository;

    protected BaseRepositoryImpl(JpaRepository<D, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void saveOrUpdate(E entity) {
        jpaRepository.save(mapToDao(entity));
    }

    protected abstract D mapToDao(E entity);
}
