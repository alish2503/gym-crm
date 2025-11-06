package com.gymcrm.domain.model;

/**
 * @author Alish
 */
public abstract class HasUserProfile {
    private Long id;
    private User userProfile;

    protected HasUserProfile(Long id, User userProfile) {
        this.id = id;
        this.userProfile = userProfile;
    }

    protected HasUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }

    protected HasUserProfile() {}

    public HasUserProfile(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(User userProfile) {
        this.userProfile = userProfile;
    }
}
