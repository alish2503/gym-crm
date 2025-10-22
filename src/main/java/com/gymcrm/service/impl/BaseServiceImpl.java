package com.gymcrm.service.impl;

import com.gymcrm.dao.BaseDao;
import com.gymcrm.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Alish
 */
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final BaseDao<T, ID> dao;

    protected BaseServiceImpl(BaseDao<T,ID> dao) {
        this.dao = dao;
    }

    @Override
    public List<T> getAll() {
        log.debug("Fetching all entities of type {}", dao.getClass().getSimpleName());
        return dao.findAll();
    }
}
