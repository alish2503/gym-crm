package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import com.gymcrm.infrastructure.persistence.storage.InMemoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Alish
 */
@Repository
public class TraineeRepositoryImpl extends UserRepositoryImpl<Trainee, TraineeDao> implements TraineeRepository {

    @Autowired
    public TraineeRepositoryImpl(InMemoryStorage storage) {
        super(storage, "trainees");
    }

    @Override
    public void delete(String username) {
        storageMap.remove(username);
    }

    @Override
    protected TraineeDao mapToDao(Trainee entity) {
        return TraineeMapper.toDao(entity);
    }

    @Override
    protected Trainee mapToDomain(TraineeDao dao) {
        return TraineeMapper.ToDomain(dao);
    }
}
