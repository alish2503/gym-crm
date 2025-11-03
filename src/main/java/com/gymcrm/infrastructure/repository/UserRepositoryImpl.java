package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.port.UserRepository;
import jakarta.persistence.EntityManager;

import java.util.Optional;

/**
 * @author Alish
 */
abstract class UserRepositoryImpl<E, D>
        extends BaseRepositoryImpl<E, D> implements UserRepository<E> {

    private final Class<D> daoClass;

    protected UserRepositoryImpl(EntityManager entityManager, Class<D> daoClass) {
        super(entityManager);
        this.daoClass = daoClass;

    }

    @Override
    public void update(E user) {
        entityManager.merge(mapToDao(user));
    }

    @Override
    public boolean existsByUserName(String userName) {
        return findByUserName(userName).isPresent();
    }

    @Override
    public Optional<E> findByUserName(String userName) {
        String jpql = "from " + getDaoClass().getSimpleName() + "t where t.user.userName = :uName";
        return findDao(userName, jpql).map(this::mapToDomain);
    }

    protected Optional<D> findDao(String userName, String jpql) {
        return entityManager.createQuery(jpql, getDaoClass()).
                setParameter("uName", userName).getResultStream().findFirst();
    }

    protected Class<D> getDaoClass() {
        return daoClass;
    }

    protected abstract E mapToDomain(D dao);
}
