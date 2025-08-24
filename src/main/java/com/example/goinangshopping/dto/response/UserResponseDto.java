package com.example.goinangshopping.dto.response;

import com.example.goinangshopping.model.User;
import lombok.Data;

import javax.management.relation.Role;
import java.util.Set;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private Set<User.Roles> roles;
}