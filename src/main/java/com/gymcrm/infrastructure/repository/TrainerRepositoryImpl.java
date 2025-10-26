package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.infrastructure.assembler.TrainerAssembler;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Alish
 */
@Repository
public class TrainerRepositoryImpl extends UserRepositoryImpl<Trainer, TrainerDao> implements TrainerRepository {

    private final TrainerAssembler assembler;

    @Autowired
    public TrainerRepositoryImpl(InMemoryStorage storage, TrainerAssembler assembler) {
        super(storage, "trainers");
        this.assembler = assembler;
    }

    @Override
    protected TrainerDao mapToDao(Trainer entity) {
        return TrainerMapper.toDao(entity);
    }

    @Override
    protected Trainer mapToDomain(TrainerDao dao) {
        return assembler.mapToDomain(dao);
    }
}
