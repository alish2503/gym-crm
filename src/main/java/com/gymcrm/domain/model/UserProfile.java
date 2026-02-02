package com.gymcrm.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alish
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserProfile {
    private Long id;
    private User user;
    protected UserProfile(User user) {
        this.user = user;
    }
    public UserProfile(Long id) {
        this.id = id;
    }
}
