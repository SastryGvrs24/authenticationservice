package com.mtechproject.gsastry.authenticationservice.service;

import com.mtechproject.gsastry.authenticationservice.domain.AppUser;
import com.mtechproject.gsastry.authenticationservice.domain.UserDetailsImpl;
import com.mtechproject.gsastry.authenticationservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection for PasswordEncoder
    public CustomAuthenticationProvider(UserRepository userRepository,
                                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        try {
            UserDetails customerDetails = loadCustomerByUsername(username);
            if (passwordEncoder.matches(password, customerDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(customerDetails, password, customerDetails.getAuthorities());
            }
        } catch (UsernameNotFoundException e) {
            // If customer not found, try RestaurantOwner
        }

        return null;
    }

    private UserDetails loadCustomerByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new UserDetailsImpl(user);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
