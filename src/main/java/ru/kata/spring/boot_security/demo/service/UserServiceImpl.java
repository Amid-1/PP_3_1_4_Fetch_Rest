package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.*;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.model.AppUser;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public UserFormDto getUserFormById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toFormDto)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    @Transactional
    public void createUser(UserFormCreateDto dto) {
        AppUser user = userMapper.fromCreateDto(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = dto.getRoleIds().stream()
                    .map(id -> roleRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Роль с ID " + id + " не найдена")))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(UserFormUpdateDto dto) {
        AppUser existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        // Обновляем базовые поля (без ролей и пароля)
        existingUser.setUsername(dto.getUsername());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());

        // Обновляем пароль, если введён
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Устанавливаем роли вручную
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = dto.getRoleIds().stream()
                    .map(id -> roleRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Роль с ID " + id + " не найдена")))
                    .collect(Collectors.toSet());
            existingUser.setRoles(roles);
        }

        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void register(UserFormDto dto) {
        AppUser user = userMapper.fromFormDto(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    public List<UserDto> findByRole(String roleName) {
        return userRepository.findByRoles_Name(roleName).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
