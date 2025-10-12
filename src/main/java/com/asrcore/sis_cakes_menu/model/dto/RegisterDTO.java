package com.asrcore.sis_cakes_menu.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(@NotBlank String login, @NotBlank String name, @NotBlank String phoneNumber, @NotBlank @Size(min = 8) String password) {
}
