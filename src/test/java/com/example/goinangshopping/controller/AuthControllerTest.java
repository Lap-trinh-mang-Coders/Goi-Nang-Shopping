package com.example.goinangshopping.controller;

import com.example.goinangshopping.dto.request.LoginRequest;
import com.example.goinangshopping.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthControllerTest {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest  loginRequest) throws Exception {
        return  ResponseEntity.ok(authService.login(loginRequest));
    }
}
