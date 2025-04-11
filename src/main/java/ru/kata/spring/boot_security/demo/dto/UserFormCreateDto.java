package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.validation.Marker;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class UserFormCreateDto extends AbstractUserDto {

    @Override
    @Size(min = 3, message = "Пароль должен быть не менее 3 символов", groups = Marker.OnCreate.class)
    @NotBlank(message = "Пароль обязателен", groups = Marker.OnCreate.class)
    public String getPassword() {
        return password;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFormCreateDto)) return false;
        UserFormCreateDto that = (UserFormCreateDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}