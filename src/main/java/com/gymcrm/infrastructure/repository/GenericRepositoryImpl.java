package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.port.GenericRepository;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
public abstract class GenericRepositoryImpl<E, D, ID> implements GenericRepository<E> {

    protected final Map<ID, D> storageMap;

    protected GenericRepositoryImpl(InMemoryStorage storage, String namespace) {
        this.storageMap = storage.getNamespace(namespace);
    }

    @Override
    public List<E> findAll() {
        return new ArrayList<>(storageMap.values()).stream().map(this::mapToDomain).toList();
    }
    protected abstract E mapToDomain(D dao);
}
