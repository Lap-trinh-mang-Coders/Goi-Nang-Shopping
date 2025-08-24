package com.example.goinangshopping.controller;

import com.example.goinangshopping.dto.request.RegisterRequest;
import com.example.goinangshopping.model.Role;
import com.example.goinangshopping.model.User;
import com.example.goinangshopping.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/test")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        user.getRoles().add(Role.USER);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<String> allAccess() {
        return ResponseEntity.ok("Public Content");
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> userAccess() {
        return ResponseEntity.ok("User Content");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Admin Content");
    }
}
