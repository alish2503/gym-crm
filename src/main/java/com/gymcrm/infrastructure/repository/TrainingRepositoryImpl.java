package com.gymcrm.infrastructure.repository;

import com.gymcrm.domain.model.FullName;
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
class TrainingRepositoryImpl extends BaseRepositoryImpl<Training, TrainingDao> implements TrainingRepository {

    @Autowired
    public TrainingRepositoryImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Training> getTrainingsForTrainee(String userName, LocalDate from, LocalDate to,
                                                 FullName trainerName, TrainingTypeEnum typeEnum) {

        String jpql = "select tr from TrainingDao tr join fetch tr.trainer trainer" +
                    "where tr.trainee.user.userName = :uname ";

        Map<String, Object> params = new HashMap<>();
        params.put("uname", userName);
        jpql = appendDateAndNameFilters(jpql, params, from, to, trainerName, "trainer");
        if (typeEnum != null) {
            jpql += "and tr.trainingType.name = :tType ";
            params.put("tType", typeEnum);
        }
        TypedQuery<TrainingDao> query = entityManager.createQuery(jpql, TrainingDao.class);
        params.forEach(query::setParameter);
        return query.getResultList().stream().map(TrainingMapper::toDomainForTrainee).toList();
    }

    public List<Training> getTrainingsForTrainer(String userName, LocalDate from, LocalDate to,
                                                 FullName traineeName) {

        String jpql = "select tr from TrainingDao tr join fetch tr.trainee trainee" +
                    "where tr.trainer.user.userName = :uname ";

        Map<String, Object> params = new HashMap<>();
        params.put("uname", userName);
        jpql = appendDateAndNameFilters(jpql, params, from, to, traineeName, "trainee");
        TypedQuery<TrainingDao> query = entityManager.createQuery(jpql, TrainingDao.class);
        params.forEach(query::setParameter);
        return query.getResultList().stream().map(TrainingMapper::toDomainForTrainer).toList();
    }

    public boolean existsTraining(String trainerUsername, String traineeUsername,
                                  LocalDate trainingDate, String trainingName) {

        String jpql = "select t from TrainingDao t where t.trainer.user.userName = :trainerUsername "+
                    "and t.trainee.user.userName = :traineeUsername and t.trainingDate = :trainingDate "+
                    "and t.trainingName = :trainingName";

        return entityManager.createQuery(jpql, TrainingDao.class)
                .setParameter("trainerUsername", trainerUsername)
                .setParameter("traineeUsername", traineeUsername)
                .setParameter("trainingDate", trainingDate)
                .setParameter("trainingName", trainingName)
                .getResultStream()
                .findFirst()
                .isPresent();
    }


    private String appendDateAndNameFilters(String jpql, Map<String, Object> params, LocalDate from,
                                            LocalDate to, FullName name, String aliasPrefix) {

        if (from != null) {
            jpql += "and tr.trainingDate >= :from ";
            params.put("from", from);
        }
        if (to != null) {
            jpql += "and tr.trainingDate <= :to ";
            params.put("to", to);
        }
        if (name != null) {
            jpql += "and " + aliasPrefix + ".user.firstName = :fName " +
                    "and " + aliasPrefix + ".user.lastName = :lName ";
            params.put("fName", name.getFirstName());
            params.put("lName", name.getLastName());
        }
        return jpql;
    }


    @Override
    protected TrainingDao mapToDao(Training entity) {
        return TrainingMapper.toDao(entity);
    }
}

