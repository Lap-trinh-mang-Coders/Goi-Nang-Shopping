package com.example.goinangshopping.service;

import com.example.goinangshopping.model.User;
import com.example.goinangshopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // email làm username
                .password(user.getPassword())
                .authorities(
                        user.getRoles().stream()
                                .map(role -> "ROLE_" + role.name()) // ROLE_ADMIN, ROLE_CUSTOMER
                                .toArray(String[]::new)
                )
                .build();
    }
}
