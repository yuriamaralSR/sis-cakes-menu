package com.asrcore.sis_cakes_menu.model.dto;

import jakarta.validation.constraints.Size;

public record UserUpdateDTO(String login, String name, String phoneNumber, @Size(min = 8) String password) {
}
