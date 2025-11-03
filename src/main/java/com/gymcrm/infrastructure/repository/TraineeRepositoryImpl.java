package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
class TraineeRepositoryImpl extends UserRepositoryImpl<Trainee, TraineeDao> implements TraineeRepository {

    @Autowired
    TraineeRepositoryImpl(EntityManager entityManager) {
        super(entityManager, TraineeDao.class);
    }

    @Override
    public void delete(String username) {
        entityManager.createQuery("delete from trainees t where t.user.userName = :u")
                .setParameter("u", username)
                .executeUpdate();
    }

    @Override
    public Optional<Trainee> findTraineeWithTrainers(String userName) {
        String jpql = "select distinct t from TraineeDao t join fetch t.trainers tr " +
                    "join fetch tr.specialization where t.user.userName = :uName";

        return findDao(userName, jpql)
                .map(dao -> {
                    List<Trainer> trainers = dao.getTrainers().stream()
                            .map(TrainerMapper::toDomain)
                            .toList();
                    Trainee trainee = mapToDomain(dao);
                    trainee.setTrainers(trainers);
                    return trainee;
                });
    }

    @Override
    protected TraineeDao mapToDao(Trainee entity) {
        return TraineeMapper.toDao(entity);
    }

    @Override
    protected Trainee mapToDomain(TraineeDao dao) {
        return TraineeMapper.toDomain(dao);
    }
}
