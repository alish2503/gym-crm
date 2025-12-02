package com.gymcrm.infrastructure.security.details;

import com.gymcrm.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author Alish
 */
public record CustomUserDetails(User userProfile) implements UserDetails {

    @Override
    public String getUsername() {
        return userProfile.getUsername();
    }

    @Override
    public String getPassword() {
        return userProfile.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public boolean isActive() {
        return userProfile.isActive();
    }
}
