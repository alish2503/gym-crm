package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserRepository;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;

import java.util.Optional;

/**
 * @author Alish
 */
public abstract class UserRepositoryImpl<E extends User, D extends UserDao>
        extends BaseRepositoryImpl<E, D, String> implements UserRepository<E> {

    protected UserRepositoryImpl(InMemoryStorage storage, String namespace) {
        super(storage, namespace);
    }

    @Override
    public Optional<E> findByUsername(String username) {
        D dao = storageMap.get(username);
        if (dao == null) {
            return Optional.empty();
        }
        return Optional.of(mapToDomain(dao));
    }

    @Override
    public E save(E user) {
        D dao = mapToDao(user);
        storageMap.put(user.getUsername(), dao);
        return mapToDomain(dao);
    }

    @Override
    public void update(E user) {
        D dao = mapToDao(user);
        storageMap.put(dao.getUsername(), dao);
    }
}
