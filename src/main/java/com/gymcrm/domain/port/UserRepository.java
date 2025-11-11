package com.gymcrm.domain.port;

import com.gymcrm.domain.model.UserProfile;

import java.util.Optional;

/**
 * @author Alish
 */
public interface UserRepository<E extends UserProfile> extends BaseRepository<E> {
    void update(E user);
    Optional<Long> findIdByUsername(String username);
}
