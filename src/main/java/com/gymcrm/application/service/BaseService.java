package com.gymcrm.application.service;

import java.util.List;

/**
 * @author Alish
 */
public interface BaseService<T> {
    T create(T entity);
    List<T> getAll();
}
