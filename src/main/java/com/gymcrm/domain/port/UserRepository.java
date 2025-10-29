package com.gymcrm.domain.port;

import com.gymcrm.domain.model.User;
import java.util.Optional;

/**
 * @author Alish
 */
public interface UserRepository<E extends User> extends BaseRepository<E> {
    Optional<E> findByUsername(String username);
    void update(E userDao);
}
