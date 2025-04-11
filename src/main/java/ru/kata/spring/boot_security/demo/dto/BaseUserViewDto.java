package ru.kata.spring.boot_security.demo.dto;

import java.util.List;

public abstract class BaseUserViewDto {
    protected Long id;
    protected String username;
    protected String lastName;
    protected String email;
    protected List<Long> roleIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Long> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Long> roleIds) { this.roleIds = roleIds; }
}
