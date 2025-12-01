package com.gymcrm.infrastructure.security.service.impl;

import com.gymcrm.domain.model.User;
import com.gymcrm.domain.port.UserProfileRepository;
import com.gymcrm.infrastructure.security.CustomUserDetails;
import com.gymcrm.infrastructure.security.service.port.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Alish
 */
@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public CustomUserDetailsServiceImpl(UserProfileRepository userProfileRepository, PasswordEncoder encoder) {
        this.userProfileRepository = userProfileRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User userProfile = userProfileRepository.findProfileByUserName(username).orElseThrow(
                () -> new UsernameNotFoundException("Wrong username"));

        return new CustomUserDetails(userProfile);
    }

    @Override
    public boolean isValidPassword(String username, String rawPassword) {
        UserDetails userDetails = loadUserByUsername(username);
        return encoder.matches(rawPassword, userDetails.getPassword());
    }
}
