package com.gymcrm.infrastructure.persistence.dao;

import com.gymcrm.domain.model.TrainingTypeEnum;
import jakarta.persistence.*;

/**
 * @author Alish
 */
@Entity
@Table(name = "training_types")
public class TrainingTypeDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, unique=true)
    private TrainingTypeEnum name;

    public TrainingTypeDao() {
    }

    public TrainingTypeDao(TrainingTypeEnum name) {
        this.name = name;
    }

    public TrainingTypeEnum getName() {
        return name;
    }

}