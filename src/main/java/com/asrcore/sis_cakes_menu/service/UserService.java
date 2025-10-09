package com.asrcore.sis_cakes_menu.service;

import com.asrcore.sis_cakes_menu.infra.exception.DuplicateLoginException;
import com.asrcore.sis_cakes_menu.infra.exception.InconsistentDataException;
import com.asrcore.sis_cakes_menu.infra.exception.UserNotFoundException;
import com.asrcore.sis_cakes_menu.model.User;
import com.asrcore.sis_cakes_menu.model.dto.RegisterDTO;
import com.asrcore.sis_cakes_menu.model.dto.UserResponseDTO;
import com.asrcore.sis_cakes_menu.model.dto.UserUpdateDTO;
import com.asrcore.sis_cakes_menu.model.enums.UserRole;
import com.asrcore.sis_cakes_menu.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO registerUser(RegisterDTO RegisterData) {
        if (this.userRepository.findByLogin(RegisterData.login()) != null) {
           throw new DuplicateLoginException("This login " +  RegisterData.login() + " is already in use.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(RegisterData.password());
        UserRole role = UserRole.USER;
        User newUser = new User(RegisterData.login(), RegisterData.name(), RegisterData.phoneNumber(), encryptedPassword,  role);

        this.userRepository.save(newUser);

        return new UserResponseDTO(newUser.getId(), newUser.getLogin(), newUser.getName(), newUser.getPhoneNumber(), newUser.getRole());
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        List<UserResponseDTO> usersResponse = new ArrayList<>();
        for (User user : users) {
            usersResponse.add(new UserResponseDTO(user.getId(), user.getLogin(), user.getName(), user.getPhoneNumber(), user.getRole()));
        }
        return  usersResponse;
    }
}
