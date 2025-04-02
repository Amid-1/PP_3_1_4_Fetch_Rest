package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserFormCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserFormUpdateDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserFormDto getUserFormById(Long id);

    void createUser(UserFormCreateDto dto);
    void updateUser(UserFormUpdateDto dto);
    void deleteUser(Long id);
    void register(UserFormDto dto);
}
