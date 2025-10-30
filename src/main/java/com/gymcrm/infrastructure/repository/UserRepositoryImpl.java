package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Optional;

/**
 * @author Alish
 */
abstract class UserRepositoryImpl<E extends User, D>
        extends BaseRepositoryImpl<E, D> implements UserRepository<E> {

    protected UserRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public E update(E user) {
        D dao = mapToDao(user);
        return mapToDomain(entityManager.merge(dao));
    }

    protected Optional<E> findByUserName(String username, String daoName) {
        String jpql = "from " + daoName + "t where t.user.userName = :u";
        Query query = entityManager.createQuery(jpql).
                setParameter("u", username);
        return getSingleResultOrEmpty(query);
    }
}
