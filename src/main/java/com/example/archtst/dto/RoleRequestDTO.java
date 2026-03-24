package com.example.archtst.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequestDTO(
        @NotBlank(message = "Название роли не может быть пустым")
        String roleName // Например: "ROLE_ADMIN"
) {}