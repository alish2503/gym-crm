package com.gymcrm.infrastructure.persistence.dao;

/**
 * @author Alish
 */
public class TrainerDao extends UserDao {
    private String specialization;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
