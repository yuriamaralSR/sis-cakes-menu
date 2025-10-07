package com.asrcore.sis_cakes_menu.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(@NotBlank String login, @NotBlank String name, @NotBlank String phoneNumber, @NotBlank String password) {
}
