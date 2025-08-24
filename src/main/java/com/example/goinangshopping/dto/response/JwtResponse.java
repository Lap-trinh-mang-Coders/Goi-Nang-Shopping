package com.example.goinangshopping.dto.response;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
public class JwtResponse {
    private String jwtToken;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;
}
