package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.port.GenericRepository;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;

import java.util.Map;

/**
 * @author Alish
 */
public abstract class GenericRepositoryImpl<E, D, ID> implements GenericRepository<E> {

    protected final Map<ID, D> storageMap;

    protected GenericRepositoryImpl(InMemoryStorage storage, String namespace) {
        this.storageMap = storage.getNamespace(namespace);
    }

    protected abstract E mapToDomain(D dao);
}
