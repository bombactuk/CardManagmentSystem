package com.bank.cardManagementSystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/unsecured")
    public String unsecuredDate() {
        return "Unsecured date";
    }

    @GetMapping("/secured")
    public String securedDate() {
        return "Secured date";
    }

    @GetMapping("/admin")
    public String adminDate() {
        return "Admin date";
    }

    @GetMapping("/info")
    public String userDate(Principal principal) {
        return principal.getName();
    }

}
