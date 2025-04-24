package com.mtechproject.gsastry.authenticationservice.service;

import com.mtechproject.gsastry.authenticationservice.domain.AppUser;
import com.mtechproject.gsastry.authenticationservice.domain.UserDetailsImpl;
import com.mtechproject.gsastry.authenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException("User not in db");
        }

        return new UserDetailsImpl(user);

    }
}
