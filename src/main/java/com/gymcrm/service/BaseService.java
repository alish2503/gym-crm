package com.gymcrm.service;

import java.util.List;

/**
 * @author Alish
 */
public interface BaseService<T, ID> {
    T create(T entity);
    List<T> getAll();
}
