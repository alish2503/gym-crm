package com.gymcrm.domain.port;

import com.gymcrm.domain.model.User;

import java.util.Optional;

/**
 * @author Alish
 */
public interface UserProfileRepository extends BaseRepository<User> {
    Optional<User> findProfileByUsername(String username);
    boolean existsByUserName(String username);
    long countActiveUsers();
}
