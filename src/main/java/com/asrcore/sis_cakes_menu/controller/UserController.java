package com.asrcore.sis_cakes_menu.controller;

import com.asrcore.sis_cakes_menu.model.dto.RegisterDTO;
import com.asrcore.sis_cakes_menu.model.dto.UserResponseDTO;
import com.asrcore.sis_cakes_menu.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid RegisterDTO data) {
        UserResponseDTO newUser = userService.registerUser(data);

        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
