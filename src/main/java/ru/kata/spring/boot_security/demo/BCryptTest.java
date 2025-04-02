package ru.kata.spring.boot_security.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "petrov";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
    }
}

//  admin@mail.com     $2a$10$dBw8TifldjajFkO76VkpneeNJifjAmL07DOolK67/yGs88A7K9tvO
//  user@mail.com     $2a$10$6MEUFxJrRq4pPINjq6EkJ.LCKFm1PUBbz.DamL/AwMIi4pIA0m/E.
