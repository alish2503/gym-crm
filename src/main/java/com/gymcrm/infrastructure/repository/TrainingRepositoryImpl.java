package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.infrastructure.assembler.TrainingAssembler;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.mapper.TrainingMapper;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TrainingRepositoryImpl extends BaseRepositoryImpl<Training, TrainingDao, Long> implements TrainingRepository {

    private final TrainingAssembler assembler;
    private long idCounter = 0;

    @Autowired
    public TrainingRepositoryImpl(InMemoryStorage storage, TrainingAssembler assembler) {
        super(storage, "trainings");
        this.assembler = assembler;
        if (!storageMap.isEmpty()) {
            idCounter = storageMap.keySet().stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0L);
        }
    }

    @Override
    public Training save(Training training) {
        TrainingDao dao = mapToDao(training);
        dao.setId(++idCounter);
        storageMap.put(dao.getId(), dao);
        return mapToDomain(dao);
    }

    @Override
    public Optional<Training> findById(Long id) {
        TrainingDao dao = storageMap.get(id);
        if (dao == null) {
            return Optional.empty();
        }
        return Optional.of(mapToDomain(dao));
    }

    @Override
    protected TrainingDao mapToDao(Training entity) {
        return TrainingMapper.toDao(entity);
    }

    @Override
    protected Training mapToDomain(TrainingDao dao) {
        return assembler.mapToDomain(dao);
    }
}

