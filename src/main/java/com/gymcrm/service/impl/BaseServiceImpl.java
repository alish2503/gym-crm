package com.gymcrm.service.impl;

import com.gymcrm.dao.BaseDao;
import com.gymcrm.service.BaseService;

import java.util.List;

/**
 * @author Alish
 */
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected final BaseDao<T, ID> dao;

    protected BaseServiceImpl(BaseDao<T,ID> dao) {
        this.dao = dao;
    }

    @Override
    public List<T> getAll() {
        return dao.findAll();
    }
}
