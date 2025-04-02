package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserFormCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserFormUpdateDto;
import ru.kata.spring.boot_security.demo.model.AppUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto toDto(AppUser user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRoleIds(toRoleIds(user.getRoles()));
        return dto;
    }

    public UserFormDto toFormDto(AppUser user) {
        if (user == null) return null;

        UserFormDto dto = new UserFormDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRoleIds(toRoleIds(user.getRoles()));
        return dto;
    }

    public AppUser fromFormDto(UserFormDto dto) {
        if (dto == null) return null;

        AppUser user = new AppUser();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRoles(fromRoleIds(dto.getRoleIds()));
        return user;
    }

    public AppUser fromCreateDto(UserFormCreateDto dto) {
        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRoles(fromRoleIds(dto.getRoleIds()));
        return user;
    }

    public AppUser fromUpdateDto(UserFormUpdateDto dto, AppUser existingUser) {
        existingUser.setUsername(dto.getUsername());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(dto.getPassword());
        }

        existingUser.setRoles(fromRoleIds(dto.getRoleIds()));
        return existingUser;
    }

    public UserFormUpdateDto toUpdateDto(UserDto userDto) {
        UserFormUpdateDto dto = new UserFormUpdateDto();
        dto.setId(userDto.getId());
        dto.setUsername(userDto.getUsername());
        dto.setLastName(userDto.getLastName());
        dto.setEmail(userDto.getEmail());
        dto.setRoleIds(userDto.getRoleIds());
        return dto;
    }

    private List<Long> toRoleIds(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toList());
    }

    private Set<Role> fromRoleIds(Collection<Long> roleIds) {
        if (roleIds == null) return null;
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }
}