package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.port.BaseRepository;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */
public abstract class BaseRepositoryImpl<E, D, ID> extends GenericRepositoryImpl<E, D, ID> implements BaseRepository<E> {

    public BaseRepositoryImpl(InMemoryStorage storage, String namespace) {
        super(storage, namespace);
    }

    @Override
    public List<E> findAll() {
        return new ArrayList<>(storageMap.values()).stream().map(this::mapToDomain).toList();
    }
    protected abstract D mapToDao(E entity);
}
