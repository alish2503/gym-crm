package com.gymcrm.domain.port;

import java.util.List;

/**
 * @author Alish
 */

public interface GenericRepository<E> {
    List<E> findAll();
}
