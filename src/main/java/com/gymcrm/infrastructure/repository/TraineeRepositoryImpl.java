package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.port.TraineeRepository;
import com.gymcrm.infrastructure.persistence.dao.TraineeDao;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TraineeRepositoryImpl extends UserRepositoryImpl<Trainee, TraineeDao> implements TraineeRepository {

    @Autowired
    TraineeRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return findByUserName(username, "TraineeDao");
    }

    @Override
    public void delete(String username) {
        entityManager.createQuery("delete from trainees t where t.user.userName = :u")
                .setParameter("u", username)
                .executeUpdate();
    }

    @Override
    protected TraineeDao mapToDao(Trainee entity) {
        return TraineeMapper.toDao(entity);
    }

    @Override
    protected Trainee mapToDomain(TraineeDao dao) {
        return TraineeMapper.toDomain(dao.getUser(), dao.getDateOfBirth(), dao.getAddress());
    }
}
