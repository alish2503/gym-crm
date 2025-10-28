package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.port.BaseRepository;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;

/**
 * @author Alish
 */
public abstract class BaseRepositoryImpl<E, D, ID> extends GenericRepositoryImpl<E, D, ID> implements BaseRepository<E> {

    public BaseRepositoryImpl(InMemoryStorage storage, String namespace) {
        super(storage, namespace);
    }
    protected abstract D mapToDao(E entity);
}
