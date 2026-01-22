package com.gymcrm.infrastructure.persistence.adapter;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.persistence.jpa.TrainingTypeJpaRepository;
import com.gymcrm.infrastructure.persistence.mapper.TrainingTypeDaoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
    private final TrainingTypeJpaRepository trainingTypeJpaRepository;

    @Autowired
    public TrainingTypeRepositoryImpl(TrainingTypeJpaRepository trainingTypeJpaRepository) {
        this.trainingTypeJpaRepository = trainingTypeJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrainingType> findByName(TrainingTypeEnum typeEnum) {
        return trainingTypeJpaRepository.findByName(typeEnum).map(TrainingTypeDaoMapper::toDomain);
    }

    @Override
    public List<TrainingType> findAll() {
       return trainingTypeJpaRepository.findAll().stream().map(TrainingTypeDaoMapper::toDomain).toList();
    }

    @Override
    public boolean existsByName(TrainingTypeEnum typeEnum) {
        return trainingTypeJpaRepository.existsByName(typeEnum);
    }

    @Override
    public long count() {
        return trainingTypeJpaRepository.count();
    }
}
