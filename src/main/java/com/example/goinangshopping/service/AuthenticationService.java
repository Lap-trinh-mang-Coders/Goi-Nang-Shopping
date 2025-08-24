package com.example.goinangshopping.service;

import com.example.goinangshopping.dto.request.LoginRequest;
import com.example.goinangshopping.dto.request.RegisterRequest;
import com.example.goinangshopping.dto.response.LoginResponse;
import com.example.goinangshopping.dto.response.RegisterResponse;
import com.example.goinangshopping.exceptions.AppException;
import com.example.goinangshopping.exceptions.ErrorCode;
import com.example.goinangshopping.model.User;
import com.example.goinangshopping.repository.UserRepository;
import com.example.goinangshopping.security.JwtUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtUtils jwtUtils,
                                 PasswordEncoder passwordEncoder,
                                 UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);
        String role = jwtUtils.extractRole(token);
        return new LoginResponse(token, role);

    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_INVALID);
        }
        // ✅ Kiểm tra password và confirmPassword
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRM_NOT_MATCH); // bạn có thể định nghĩa mã lỗi này trong ErrorCode
        }
        User user = User.builder()
                .fullname(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Collections.singleton(User.Roles.GUEST))
                .build();

        userRepository.save(user);

        return new RegisterResponse("User registered successfully", user.getEmail());
    }
}
