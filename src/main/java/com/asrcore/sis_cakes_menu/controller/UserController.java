package com.asrcore.sis_cakes_menu.controller;

import com.asrcore.sis_cakes_menu.model.dto.RegisterDTO;
import com.asrcore.sis_cakes_menu.model.dto.UserResponseDTO;
import com.asrcore.sis_cakes_menu.model.dto.UserUpdateDTO;
import com.asrcore.sis_cakes_menu.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
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

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserById(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO data) {
        UserResponseDTO updatedUser = userService.updateUser(id, data);
        return ResponseEntity.ok(updatedUser);
    }
}
