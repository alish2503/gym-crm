package com.gymcrm.infrastructure.persistence.jpa;

import com.gymcrm.infrastructure.persistence.dao.UserDao;
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
