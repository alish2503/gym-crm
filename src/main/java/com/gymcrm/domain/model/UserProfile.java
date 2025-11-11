package com.gymcrm.domain.model;

/**
 * @author Alish
 */
public abstract class UserProfile {
    private Long id;
    private User user;

    protected UserProfile(Long id, User user) {
        this.id = id;
        this.user = user;
    }

    protected UserProfile(User user) {
        this.user = user;
    }

    protected UserProfile() {}

    public UserProfile(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
