package com.asrcore.sis_cakes_menu.service;

import com.asrcore.sis_cakes_menu.infra.exception.DuplicateLoginException;
import com.asrcore.sis_cakes_menu.model.User;
import com.asrcore.sis_cakes_menu.model.dto.RegisterDTO;
import com.asrcore.sis_cakes_menu.model.dto.UserResponseDTO;
import com.asrcore.sis_cakes_menu.model.enums.UserRole;
import com.asrcore.sis_cakes_menu.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        User newUser = new User(RegisterData.login(), RegisterData.name(), encryptedPassword,  role);

        this.userRepository.save(newUser);

        return new UserResponseDTO(newUser.getId(), newUser.getLogin(), newUser.getName(), newUser.getRole());
    }
}
