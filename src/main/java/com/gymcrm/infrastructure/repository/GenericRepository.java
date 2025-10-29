package com.gymcrm.infrastructure.repository;

import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;

import java.util.Map;

/**
 * @author Alish
 */
abstract class GenericRepository<E, D, ID> {

    protected final Map<ID, D> storageMap;

    protected GenericRepository(InMemoryStorage storage, String namespace) {
        this.storageMap = storage.getNamespace(namespace);
    }
    protected abstract E mapToDomain(D dao);
}
