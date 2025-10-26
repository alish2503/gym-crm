package com.gymcrm.service;

import com.gymcrm.domain.port.BaseRepository;
import com.gymcrm.application.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Alish
 */
public abstract class BaseServiceImpl<E> implements BaseService<E> {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final BaseRepository<E> baseRepository;

    protected BaseServiceImpl(BaseRepository<E> baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    public List<E> getAll() {
        log.debug("Fetching all entities of type {}", baseRepository.getClass().getSimpleName());
        return baseRepository.findAll();
    }
}
