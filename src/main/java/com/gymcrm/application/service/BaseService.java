package com.gymcrm.application.service;

/**
 * @author Alish
 */
interface BaseService<T> {
    T create(T entity);
}
