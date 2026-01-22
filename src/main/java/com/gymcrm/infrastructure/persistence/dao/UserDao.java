package com.gymcrm.infrastructure.persistence.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Alish
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_profile")
public class UserDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, name = "first_name")
    private String firstName;

    @Column(nullable=false, name = "last_name")
    private String lastName;

    @Column(nullable=false, name = "is_active")
    private boolean isActive;
}
