package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.HasUserProfile;
import com.gymcrm.domain.port.UserRepository;

import java.util.Optional;

/**
 * @author Alish
 */
abstract class UserRepositoryImpl<E extends HasUserProfile, D> extends BaseRepositoryImpl<E, D> implements UserRepository<E> {
    private final Class<D> daoClass;

    protected UserRepositoryImpl(Class<D> daoClass) {
        this.daoClass = daoClass;
    }

    @Override
    public void update(E user) {
        entityManager.merge(mapToDao(user));
    }

    @Override
    public Optional<Long> findIdByUsername(String username) {
        String jpql = "select t.id from " + daoClass.getSimpleName() + " t where t.user.username = :uName";
        return entityManager.createQuery(jpql, Long.class).
                setParameter("uName", username).getResultStream().findFirst();
    }
}
