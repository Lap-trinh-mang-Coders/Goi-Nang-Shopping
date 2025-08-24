package com.example.goinangshopping.controller;

import com.example.goinangshopping.service.AuthService;
import com.example.goinangshopping.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Them, sua, xoa Role
    @Autowired
    private AuthService authService;
    @Autowired
    private AuthorService authorService;

    // Admin thay doi cac thong tin cua user:

}
