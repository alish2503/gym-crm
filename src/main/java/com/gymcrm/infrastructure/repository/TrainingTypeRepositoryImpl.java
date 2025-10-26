package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingTypeRepository;
import com.gymcrm.infrastructure.persistence.dao.TrainingTypeDao;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TrainingTypeRepositoryImpl extends GenericRepositoryImpl<TrainingType, TrainingTypeDao, Long> implements TrainingTypeRepository {

    @Autowired
    public TrainingTypeRepositoryImpl(InMemoryStorage storage) {
        super(storage, "trainingTypes");
    }

    @Override
    public Optional<TrainingType> findByName(String name) {
        return storageMap.values().stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst().map(this::mapToDomain);
    }

    @Override
    protected TrainingType mapToDomain(TrainingTypeDao dao) {
        return new TrainingType(TrainingTypeEnum.valueOf(dao.getName()));
    }
}
