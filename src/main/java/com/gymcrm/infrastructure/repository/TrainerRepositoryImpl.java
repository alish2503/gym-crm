package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Trainee;
import com.gymcrm.domain.model.Trainer;
import com.gymcrm.domain.model.TrainingType;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainerRepository;
import com.gymcrm.infrastructure.mapper.TraineeMapper;
import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import com.gymcrm.infrastructure.mapper.TrainerMapper;
import com.gymcrm.infrastructure.persistence.dao.UserDao;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */
@Repository
public class TrainerRepositoryImpl extends UserRepositoryImpl<Trainer, TrainerDao> implements TrainerRepository {

    @Autowired
    public TrainerRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return findByUserName(username, "TrainerDao");
    }

    @Override
    public List<Trainer> getAvailableTrainersNotAssigned(Trainee trainee) {
        List<Long> assigned = TraineeMapper.toDao(trainee).getTrainers().stream().map(TrainerDao::getId).toList();
        String jpql = "select t from TrainerDao t join fetch t.specialization where t.id not in :assigned";
        return entityManager.createQuery(jpql, TrainerDao.class)
                .setParameter("assigned", assigned)
                .getResultList()
                .stream()
                .map(this::mapToDomain)
                .toList();
    }

    @Override
    public List<Trainer> findAllByUserNameIn(List<String> userNames) {
        String jpql = "select t from TrainerDao t join fetch t.specialization where t.user.userName in :userNames";
        return entityManager.createQuery(jpql, TrainerDao.class).
                setParameter("userNames", userNames).
                getResultList().stream().
                map(this::mapToDomain).
                toList();
    }

    @Override
    protected TrainerDao mapToDao(Trainer entity) {
        return TrainerMapper.toDao(entity);
    }

    @Override
    protected Trainer mapToDomain(TrainerDao dao) {
        UserDao userDao = dao.getUser();
        TrainingTypeEnum typeEnum = dao.getSpecialization().getName();
        TrainingType type = new TrainingType(typeEnum);
        return TrainerMapper.toDomain(userDao, type);
    }
}
