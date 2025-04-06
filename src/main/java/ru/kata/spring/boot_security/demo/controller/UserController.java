package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.dto.UserFormCreateDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserFormUpdateDto;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          RoleRepository roleRepository,
                          UserMapper userMapper) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String listUsers(@RequestParam(value = "filter", required = false) String filter,
                            Model model,
                            Principal principal) {
        List<UserDto> userDtos;
        if ("ADMIN".equalsIgnoreCase(filter)) {
            userDtos = userService.findByRole("ROLE_ADMIN");
        } else if ("USER".equalsIgnoreCase(filter)) {
            userDtos = userService.findByRole("ROLE_USER");
        } else {
            userDtos = userService.getAllUsers();
        }

        model.addAttribute("filter", filter);
        model.addAttribute("userDtos", userDtos);
        model.addAttribute("userForm", new UserFormCreateDto());
        model.addAttribute("roles", roleRepository.findAll());

        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        model.addAttribute("user", userDetails);

        return "admin";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("userForm") @Valid UserFormCreateDto userForm,
                           BindingResult bindingResult,
                           Model model,
                           Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDtos", userService.getAllUsers());
            model.addAttribute("roles", roleRepository.findAll());

            UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
            model.addAttribute("user", userDetails);

            return "admin";
        }

        try {
            userService.createUser(userForm);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при создании пользователя: " + e.getMessage());
            return "admin";
        }

        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("userDto") @Valid UserFormUpdateDto userDto,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userDtos", userService.getAllUsers());
            model.addAttribute("roles", roleRepository.findAll());
            return "admin";
        }
        userService.updateUser(userDto);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("userDto") UserFormDto userDto, Model model) {
        Long id = userDto.getId();
        if (id == null) {
            model.addAttribute("error", "Ошибка: ID пользователя не передан.");
            return "admin";
        }

        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            model.addAttribute("error", "Не удалось удалить пользователя: " + e.getMessage());
            return "admin";
        }

        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        UserFormUpdateDto userDto = userMapper.toUpdateDto(userService.getUserById(id));
        model.addAttribute("userDto", userDto);
        model.addAttribute("roles", roleRepository.findAll());
        return "edit-user";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserFormDto dto = new UserFormDto();
        roleRepository.findByName("ROLE_USER").ifPresent(role ->
                dto.setRoleIds(List.of(role.getId()))
        );

        model.addAttribute("userDto", dto);
        model.addAttribute("roles", roleRepository.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDto") @Valid UserFormDto userDto,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        userService.register(userDto);
        return "redirect:/login";
    }



    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserFormCreateDto userForm) {
        System.out.println(">>> POST /api/users: " + userForm.getEmail() + " — " + userForm.getRoleIds());
        try {
            userService.createUser(userForm);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }
}
