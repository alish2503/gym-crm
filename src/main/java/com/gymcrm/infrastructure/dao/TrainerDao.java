package com.gymcrm.infrastructure.dao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainer")
public class TrainerDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", unique=true, nullable=false)
    private UserDao user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="specialization_id")
    private TrainingTypeDao specialization;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY)
    List<TraineeDao> trainees = new ArrayList<>();

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    List<TrainingDao> trainings = new ArrayList<>();

    public TrainerDao(Long id, UserDao user, TrainingTypeDao specialization) {
        this.id = id;
        this.user = user;
        this.specialization = specialization;
    }

    public TrainerDao(Long id) {
        this.id = id;
    }
}
