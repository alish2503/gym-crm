package com.gymcrm.domain.port;

import com.gymcrm.domain.model.HasUserProfile;
import com.gymcrm.domain.model.User;

import java.util.Optional;

/**
 * @author Alish
 */
public interface UserRepository<E extends HasUserProfile> extends BaseRepository<E> {
    void update(E user);
    Optional<Long> findIdByUsername(String username);
}
