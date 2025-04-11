package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterDto extends AbstractUserDto {

    @Override
    @Size(min = 3, message = "Пароль должен быть не менее 3 символов")
    @NotBlank(message = "Пароль обязателен")
    public String getPassword() {
        return password;
    }
}
