package com.gymcrm.infrastructure.persistence.adapter;

import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.infrastructure.persistence.jpa.UserProfileJpaRepository;
import com.gymcrm.infrastructure.persistence.mapper.UserDaoMapper;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class UserProfileRepositoryImpl extends BaseRepositoryImpl<User, UserDao> implements UserProfileRepository {
    private final UserProfileJpaRepository userProfileJpaRepository;

    @Autowired
    public UserProfileRepositoryImpl(UserProfileJpaRepository userProfileJpaRepository) {
        super(userProfileJpaRepository);
        this.userProfileJpaRepository = userProfileJpaRepository;
    }

    @Override
    public Optional<User> findProfileByUsername(String username) {
        return userProfileJpaRepository.findByUsername(username).map(UserDaoMapper::toDomain);
    }

    @Override
    public boolean existsByUserName(String username) {
       return userProfileJpaRepository.existsByUsername(username);
    }

    @Override
    public long countActiveUsers() {
        return userProfileJpaRepository.countByIsActiveTrue();
    }

    @Override
    protected UserDao mapToDao(User userProfile) {
        return UserDaoMapper.toDao(userProfile);
    }
}
