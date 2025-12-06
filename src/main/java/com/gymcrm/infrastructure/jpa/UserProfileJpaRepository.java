package com.gymcrm.infrastructure.jpa;

import com.gymcrm.infrastructure.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Alish
 */

public interface UserProfileJpaRepository extends JpaRepository<UserDao, Long> {
    Optional<UserDao> findByUsername(String username);
    boolean existsByUsername(String username);
    long countByIsActiveTrue();
}
