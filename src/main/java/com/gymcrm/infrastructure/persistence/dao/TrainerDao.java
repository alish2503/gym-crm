package com.gymcrm.infrastructure.persistence.dao;

import jakarta.persistence.*;

/**
 * @author Alish
 */
@Entity
public class TrainerDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="user_id", unique=true, nullable=false)
    private UserDao user;

    @ManyToOne(optional = false)
    private TrainingTypeDao specialization;

    public TrainerDao() {
    }

    public TrainerDao(TrainingTypeDao specialization) {
        this.specialization = specialization;
    }

    public UserDao getUser() {
        return user;
    }

    public TrainingTypeDao getSpecialization() {
        return specialization;
    }
}
