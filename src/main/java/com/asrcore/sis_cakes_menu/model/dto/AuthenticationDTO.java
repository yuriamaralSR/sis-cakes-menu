package com.asrcore.sis_cakes_menu.model.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationDTO(@NotNull String login, @NotNull String password) {
}
