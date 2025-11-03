package com.gymcrm.domain.port;

import java.util.Optional;

/**
 * @author Alish
 */
public interface UserRepository<E> {
    Optional<E> findByUserName(String userName);
    void update(E user);
    boolean existsByUserName(String userName);
}
