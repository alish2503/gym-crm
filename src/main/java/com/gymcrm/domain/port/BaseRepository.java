package com.gymcrm.domain.port;

import java.util.List;

/**
 * @author Alish
 */
public interface BaseRepository<E> extends GenericRepository<E> {
    E save(E entity);
    List<E> findAll();
}
