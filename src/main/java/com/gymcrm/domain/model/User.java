package com.gymcrm.domain.model;

/**
 * @author Alish
 */
public class User extends FullName {
    Long id;
    private String username;
    private String password;
    private boolean isActive;

    public User(Long id, String username, String password, String firstName, String lastName, boolean isActive) {
        super(firstName, lastName);
        this.id = id;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String firstName, String lastName, boolean isActive) {
        super(firstName, lastName);
        this.username = username;
        this.password = password;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
