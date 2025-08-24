package com.example.goinangshopping.service;

import com.example.goinangshopping.model.ActionStatus;
import com.example.goinangshopping.model.Role;
import com.example.goinangshopping.model.User;
import com.example.goinangshopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class AuthorService {

    @Autowired
    private UserRepository userRepository;

    // Them Role:
    private ActionStatus addRole(String username, String role) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Role newRole;
        try {
            newRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ActionStatus.ERROR;
        }

        user.getRoles().add(newRole);
        userRepository.save(user);
        return ActionStatus.SUCCESS;
    }

    // Xoa Role:
    private ActionStatus deleteRole(String username, String role) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Role roleToDelete;
        try {
            roleToDelete = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ActionStatus.ERROR;
        }
        user.getRoles().remove(roleToDelete);
        userRepository.save(user);
        return ActionStatus.SUCCESS;
    }

    // Lay tat ca cac Roles:
    public Collection<Role> getRoles(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return user.getRoles();
    }
}
