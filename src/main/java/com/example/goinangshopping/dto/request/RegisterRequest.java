package com.example.goinangshopping.dto.request;

import com.example.goinangshopping.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username must not be blank")
    private String username;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String confirmPassword;
    private Set<User.Roles> roles;
}
