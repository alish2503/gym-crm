package com.gymcrm.infrastructure.dao;

import com.gymcrm.domain.model.TrainingTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Alish
 */
@Entity
@Table(name = "training_type")
public class TrainingTypeDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, unique=true)
    private TrainingTypeEnum name;

    public TrainingTypeDao() {}

    public TrainingTypeDao(Long id, TrainingTypeEnum name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public TrainingTypeEnum getName() {
        return name;
    }
}