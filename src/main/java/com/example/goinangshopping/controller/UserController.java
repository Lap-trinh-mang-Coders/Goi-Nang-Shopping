package com.example.goinangshopping.controller;

import com.example.goinangshopping.dto.request.ChangePasswordRequest;
import com.example.goinangshopping.dto.request.UserCreateDto;
import com.example.goinangshopping.dto.response.UserResponseDto;
import com.example.goinangshopping.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        UserResponseDto userResponseDto = userService.createUser(userCreateDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id ,@RequestBody UserCreateDto userCreateDto) {
        UserResponseDto userResponseDto = userService.updateUser(id,userCreateDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    // PUT /api/users/{id}/password - Đổi mật khẩu
    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }


    // GET /api/users/exists/username/{username} - Kiểm tra username tồn tại
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    // GET /api/users/exists/email/{email} - Kiểm tra email tồn tại
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    // GET /api/users/{id} - Lấy user theo ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // GET /api/users/username/{username} - Lấy user theo username
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.username == #username")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        UserResponseDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    // GET /api/users/email/{email} - Lấy user theo email
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }


}
