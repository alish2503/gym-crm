package com.gymcrm.domain.port;

import com.gymcrm.domain.model.User;

import java.util.Optional;

/**
 * @author Alish
 */
public interface UserProfileRepository {
    Optional<User> findProfileByUserName(String username);
    void updateProfile(User userProfile);
    boolean existsByUserName(String username);
}
