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
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
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

    public UserResponseDTO getUserById(Long id) {
        if (id == null) {
            throw new UserNotFoundException("User id is null");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated user.");
        }
        String currentLogin = authentication.getName();
        User user = this.userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!currentLogin.equals(user.getLogin()) && authentication.getAuthorities().stream().noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Unauthorized user.");
        }
        return new UserResponseDTO(user.getId(), user.getLogin(), user.getName(), user.getPhoneNumber(), user.getRole());
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (id == null) {
            throw new InconsistentDataException("User id is null.");
        }
        if (userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("Unable to delete user.");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateData) {
        if (id == null) {
            throw new InconsistentDataException("User id is null.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Unauthenticated user.");
        }
        String currentLogin = authentication.getName();
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Unable to update user."));
        if (!currentLogin.equals(userToUpdate.getLogin()) && authentication.getAuthorities().stream().noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("User not allowed to update user.");
        }
        if (userUpdateData.login() != null) {
            if (this.userRepository.findByLogin(userUpdateData.login()) != null) {
                throw new DuplicateLoginException("This login " + userUpdateData.login() + " is already in use.");
            }
            userToUpdate.setLogin(userUpdateData.login());
        }
        if (userUpdateData.password() != null) {
            if (!currentLogin.equals(userToUpdate.getLogin())) {
                throw new AccessDeniedException("Only the user himself can update his password.");
            }
            if (userUpdateData.password().length() < 8) {
                throw new InconsistentDataException("Password length is less than 8 characters.");
            }
            String encryptedPassword = new BCryptPasswordEncoder().encode(userUpdateData.password());
            userToUpdate.setPassword(encryptedPassword);
        }
        if (userUpdateData.phoneNumber() != null) {
            userToUpdate.setPhoneNumber(userUpdateData.phoneNumber());
        }
        if (userUpdateData.name() != null) {
            userToUpdate.setName(userUpdateData.name());
        }
        userRepository.save(userToUpdate);
        return new UserResponseDTO(userToUpdate.getId(), userToUpdate.getLogin(), userToUpdate.getName(), userToUpdate.getPhoneNumber(), userToUpdate.getRole());
    }
}
