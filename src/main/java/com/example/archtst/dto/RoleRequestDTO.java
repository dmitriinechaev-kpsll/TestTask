package com.example.archtst.dto;

import com.example.archtst.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDTO {
        @NotNull(message = "Название роли не может быть пустым")
        private RoleName roleName; // For example: "ROLE_ADMIN"
}