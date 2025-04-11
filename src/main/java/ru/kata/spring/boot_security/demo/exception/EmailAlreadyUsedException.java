package ru.kata.spring.boot_security.demo.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String email) {
        super("Пользователь с email " + email + " уже существует");
    }
}
