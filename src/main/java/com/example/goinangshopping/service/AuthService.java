package com.example.goinangshopping.service;

import com.example.goinangshopping.dto.request.LoginRequest;
import com.example.goinangshopping.dto.request.RegisterRequest;
import com.example.goinangshopping.dto.response.LoginResponse;
import com.example.goinangshopping.dto.response.RegisterResponse;
import com.example.goinangshopping.model.ActionStatus;
import com.example.goinangshopping.jwt.JwtUtils;
import com.example.goinangshopping.model.Role;
import com.example.goinangshopping.model.User;
import com.example.goinangshopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    // Dang ki thanh vien moi:
    public RegisterResponse register(RegisterRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            System.out.println(userRequest.getUsername() + " is already exist");
            return new RegisterResponse(
                    ActionStatus.ERROR,
                    userRequest.getUsername() + " is already exist",
                    null
            );
        }
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullname(userRequest.getFullname());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.getRoles().add(Role.USER);
        userRepository.save(user);
        return new RegisterResponse(
                ActionStatus.SUCCESS,
                user.getUsername() + " is registered Successfully!",
                user.getUsername()
        );
    }

    // Dang nhap tai khoan:
    public LoginResponse login(LoginRequest loginRequest) {
        // Tao xac thuc authentication:
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        // cho authentication nay vao SecurityContext:
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Lay thong tin user qua UserDetails:
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Tao jwt:
        String jwt = jwtUtils.generateToken(authentication);
        return new LoginResponse(jwt,
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getAuthorities()
        );
    }
}
