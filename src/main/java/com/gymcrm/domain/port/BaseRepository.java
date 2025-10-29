package com.gymcrm.domain.port;

/**
 * @author Alish
 */
public interface BaseRepository<E> {
    E save(E entity);
}
