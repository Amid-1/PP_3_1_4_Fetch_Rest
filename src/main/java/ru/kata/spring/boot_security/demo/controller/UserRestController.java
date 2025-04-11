package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public UserDto getCurrentUser(Authentication authentication) {
        return userService.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(authentication.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
