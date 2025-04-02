package ru.kata.spring.boot_security.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordCheckTest {
    public static void main(String[] args) {

        String encodedPasswordFromDB = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEaOaO9Y/LTcSKmM7rIu9Q6Ac9G6";

        String rawPassword = "admin";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(rawPassword, encodedPasswordFromDB);

        System.out.println("Пароль совпадает? " + matches);
    }

}

//  $2a$10$BhLkV1h/ce2J.A7iO9LC/Onx9xpHuCr4lqFJVeMJAUpRyXbdrtcwS
//  $2a$10$4T6hNr4HGhlMVKZu8gDzI.NC26AXCP.hRxOjWsLX0V14A2IBGxwQG

