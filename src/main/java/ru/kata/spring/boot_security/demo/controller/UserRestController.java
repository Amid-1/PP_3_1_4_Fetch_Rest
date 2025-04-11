package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserFormCreateDto;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public UserDto getCurrentUser(Authentication authentication) {
        return userService.getByEmail(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserFormCreateDto userForm) {
        userService.createUser(userForm);
        return ResponseEntity.ok().build();
    }
}
