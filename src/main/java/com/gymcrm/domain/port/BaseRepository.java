package com.gymcrm.domain.port;

/**
 * @author Alish
 */
public interface BaseRepository<E> {
    void saveOrUpdate(E entity);
}
