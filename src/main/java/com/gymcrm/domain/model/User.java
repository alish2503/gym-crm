package com.gymcrm.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alish
 */
@Getter
@Setter
@NoArgsConstructor
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

    public User(String username, String password, String firstName, String lastName) {
        super(firstName, lastName);
        this.username = username;
        this.password = password;
    }
}
