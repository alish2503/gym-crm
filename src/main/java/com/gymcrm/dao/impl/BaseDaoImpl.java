package com.gymcrm.dao.impl;

import com.gymcrm.dao.BaseDao;
import com.gymcrm.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
public abstract class BaseDaoImpl<T, ID> implements BaseDao<T, ID> {

    protected final Map<ID, T> storageMap;

    @Autowired
    protected BaseDaoImpl(InMemoryStorage storage, String namespace) {
        this.storageMap = storage.getNamespace(namespace);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storageMap.values());
    }
}
