package com.gymcrm.domain.port;

/**
 * @author Alish
 */
public interface BaseRepository<E> {
    void save(E entity);
}
