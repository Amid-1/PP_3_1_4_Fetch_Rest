package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public abstract class AbstractUserDto {

    protected Long id;

    @NotBlank(message = "Имя не может быть пустым")
    protected String username;

    @NotBlank(message = "Фамилия не может быть пустой")
    protected String lastName;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    protected String email;

    protected String password;

    protected List<Long> roleIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}

