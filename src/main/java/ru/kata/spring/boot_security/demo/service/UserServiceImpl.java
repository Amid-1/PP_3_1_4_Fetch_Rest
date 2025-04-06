package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserFormCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserFormUpdateDto;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${app.default-user-role}")
    private String defaultUserRole;

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
        User user = userMapper.fromCreateDto(dto);
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
        User existingUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        existingUser.setUsername(dto.getUsername());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void register(UserFormDto dto) {
        User user = userMapper.fromFormDto(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName(defaultUserRole)
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
