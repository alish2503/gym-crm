package com.gymcrm.infrastructure.persistence.dao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alish
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainee")
public class TraineeDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private String address;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", unique=true, nullable=false)
    private UserDao user;

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private List<TrainerDao> trainers = new ArrayList<>();

    @OneToMany(mappedBy="trainee", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<TrainingDao> trainings = new ArrayList<>();

    public TraineeDao(Long id, UserDao user, LocalDate dateOfBirth, String address) {
        this.id = id;
        this.user = user;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public TraineeDao(Long id) {
        this.id = id;
    }
}
