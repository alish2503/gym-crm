package com.gymcrm.domain.port;

/**
 * @author Alish
 */
public interface BaseRepository<E> extends GenericRepository<E> {
    E save(E entity);
}
