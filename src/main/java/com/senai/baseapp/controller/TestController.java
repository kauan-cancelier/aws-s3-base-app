package com.senai.baseapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public String publico() {
        return "Public OK";
    }

    @GetMapping("/private")
    public String privado(Authentication auth) {
        return "Usuario: " + auth.getName();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Admin OK";
    }
}