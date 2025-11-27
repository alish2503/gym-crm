package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.infrastructure.mapper.UserDaoMapper;
import com.gymcrm.infrastructure.dao.UserDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<User> findProfileByUserName(String username) {
        String jpql = "select u from UserDao u where u.username = :username";
        return entityManager.createQuery(jpql, UserDao.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .map(UserDaoMapper::toDomain);
    }

    @Override
    public void updateProfile(User userProfile) {
        entityManager.merge(UserDaoMapper.toDao(userProfile));
    }

    @Override
    public boolean existsByUserName(String username) {
        String jpql = "select count(u) from UserDao u where u.username = :uName";
        Long count = entityManager.createQuery(jpql, Long.class).setParameter("uName", username).getSingleResult();
        return count > 0;
    }
}
