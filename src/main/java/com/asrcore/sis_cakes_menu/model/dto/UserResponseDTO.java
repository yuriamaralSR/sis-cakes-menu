package com.asrcore.sis_cakes_menu.model.dto;

import com.asrcore.sis_cakes_menu.model.enums.UserRole;

public record UserResponseDTO(Long id, String login, String name, UserRole role) {
}
