package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.Training;
import com.gymcrm.domain.model.TrainingTypeEnum;
import com.gymcrm.domain.port.TrainingRepository;
import com.gymcrm.infrastructure.persistence.dao.TrainingDao;
import com.gymcrm.infrastructure.mapper.TrainingMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alish
 */
@Repository
public class TrainingRepositoryImpl extends BaseRepositoryImpl<Training, TrainingDao> implements TrainingRepository {

    @Autowired
    public TrainingRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Training> getTrainings(String username, LocalDate from, LocalDate to,
                                       String otherName, TrainingTypeEnum typeEnum) {

        String hql = "select tr from TrainingDao tr " +
                "join fetch tr.trainee trainee " +
                "join fetch tr.trainer trainer ";

        if (typeEnum != null) {
            hql += "join fetch tr.trainingType tT ";
        }
        hql += "where ";
        if (typeEnum != null) {
            hql += "trainee.user.userName = :uname ";
        } else {
            hql += "trainer.user.userName = :uname ";
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uname", username);

        if (from != null) {
            hql += "and tr.trainingDate >= :from ";
            params.put("from", from);
        }
        if (to != null) {
            hql += "and tr.trainingDate <= :to ";
            params.put("to", to);
        }
        if (otherName != null) {
            if (typeEnum != null) {
                hql += "and concat(trainer.user.firstName, ' ', trainer.user.lastName) like :tName ";
            } else {
                hql += "and concat(trainee.user.firstName, ' ', trainee.user.lastName) like :tName ";
            }
            params.put("tName", "%" + otherName + "%");
        }
        if (typeEnum != null) {
            hql += "and tT.name = :tType ";
            params.put("tType", typeEnum);
        }
        TypedQuery<TrainingDao> query = entityManager.createQuery(hql, TrainingDao.class);
        params.forEach(query::setParameter);
        return query.getResultList().stream().map(this::mapToDomain).toList();
    }

    @Override
    protected TrainingDao mapToDao(Training entity) {
        return TrainingMapper.toDao(entity);
    }

    @Override
    protected Training mapToDomain(TrainingDao dao) {
        return TrainingMapper.toDomain(dao);
    }
}

