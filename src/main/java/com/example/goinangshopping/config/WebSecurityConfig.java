package com.example.goinangshopping.config;

import com.example.goinangshopping.security.AuthTokenFilter;
import com.example.goinangshopping.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(AuthTokenFilter authTokenFilter, UserDetailsServiceImpl userDetailsService) {
        this.authTokenFilter = authTokenFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // 🔓 AUTH
                        .requestMatchers("/api/auth/**").permitAll()

                        // 🔓 PUBLIC ENDPOINTS
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/users/exists/**").permitAll()

                        // 👤 USERS
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")

                        // ☕ ITEMS
                        .requestMatchers(HttpMethod.GET, "/api/items/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/items").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/items/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/items/{id}").hasRole("ADMIN")

                        // 📦 ORDERS
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/{id}").hasAnyRole("CUSTOMER", "ADMIN")

                        // 💳 PAYMENT
                        .requestMatchers("/api/payment/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // Cho phép tất cả vào error + swagger nếu có
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // ✅ fix deprecated

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}