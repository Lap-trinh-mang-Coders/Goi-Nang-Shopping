package com.example.goinangshopping.controller;


import com.example.goinangshopping.dto.request.LoginRequest;
import com.example.goinangshopping.dto.request.RegisterRequest;
import com.example.goinangshopping.dto.response.LoginResponse;
import com.example.goinangshopping.dto.response.RegisterResponse;
import com.example.goinangshopping.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor //dùng để autoqired các bean
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = {"http://localhost:63342", "http://localhost:3000", "http://localhost:4200"},
        allowedHeaders = {"Authorization", "Content-Type", "X-Requested-With"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log lỗi , trả về thông báo lỗi
            return ResponseEntity.badRequest().body(new LoginResponse(null, "Đăng nhập thất bại: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authenticationService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse("Đăng ký thất bại: " + e.getMessage(), null));
        }
    }


}

