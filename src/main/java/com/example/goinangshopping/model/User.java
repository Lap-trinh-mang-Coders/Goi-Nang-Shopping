package com.example.goinangshopping.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String fullname;

    // This is the new field to store user roles
    @ElementCollection(fetch = FetchType.EAGER) // Fetch roles eagerly with the user
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING) // Store enum names as strings (e.g., "ADMIN", "USER")
    @Column(name = "role")
    private Set<Roles> roles = new HashSet<>(); // Initialize to avoid NullPointerException

    public enum Roles {
        ADMIN,
        USER,
        GUEST
    }

}