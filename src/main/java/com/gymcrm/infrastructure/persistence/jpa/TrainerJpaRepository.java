package com.gymcrm.infrastructure.persistence.jpa;

import com.gymcrm.infrastructure.persistence.dao.TrainerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Alish
 */


public interface TrainerJpaRepository extends JpaRepository<TrainerDao, Long> {

    @Query("select distinct t from TrainerDao t left join fetch t.trainees where t.user.username = :username")
    Optional<TrainerDao> findWithTrainees(@Param("username") String username);

    @Query("""
        select t from TrainerDao t where t.user.isActive = true and not exists (
           select tr
           from t.trainees tr
           where tr.user.username = :username
        )
    """)
    List<TrainerDao> findAvailableTrainersForTrainee(@Param("username") String username);
    List<TrainerDao> findByUserUsernameIn(List<String> usernames);
    Optional<TrainerDao> findByUserUsername(String username);
}
