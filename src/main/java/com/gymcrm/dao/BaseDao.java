package com.gymcrm.dao;

import java.util.List;

/**
 * @author Alish
 */
public interface BaseDao<T, ID> {
    T save(T entity);
    List<T> findAll();
}
