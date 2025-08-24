package com.example.goinangshopping.data;

import com.example.goinangshopping.model.Role;
import com.example.goinangshopping.model.User;
import com.example.goinangshopping.repository.UserRepository;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anhDatTaoAdmin")
public class AccountTemplate {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${secret.key1}")
    private String key1;
    @PermitAll
    @PostMapping("/taoTaiKhoan/admin/{newusername}/{newpassword}")
    public ResponseEntity<?> createAdmin(@PathVariable String newusername, @PathVariable String newpassword, @RequestParam("key") String key) {
        System.out.println("Key from YAML: '" + key1 + "'");
        if (!key.equals(key1)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        User user = new User();
        user.setUsername(newusername);
        user.setPassword(passwordEncoder.encode(newpassword));
        user.setFullname(newusername);
        user.setEmail(newusername + "@"  + newpassword + ".com");
        user.setPhone("0000000000");
        user.getRoles().add(Role.ADMIN);
        return ResponseEntity.ok(userRepository.save(user));
    }
}
