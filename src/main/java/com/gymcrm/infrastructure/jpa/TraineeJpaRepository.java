package com.gymcrm.infrastructure.jpa;

import com.gymcrm.infrastructure.dao.TraineeDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

/**
 * @author Alish
 */

public interface TraineeJpaRepository extends JpaRepository<TraineeDao, Long> {

    @Query("select distinct t from TraineeDao t left join fetch t.trainers where t.user.username = :username")
    Optional<TraineeDao> findWithTrainers(String username);

    @Query("select t.id from TraineeDao t where t.user.username = :username")
    Optional<Long> findIdByUsername(@Param("username") String username);
    Optional<TraineeDao> findByUserUsername(String username);
}
