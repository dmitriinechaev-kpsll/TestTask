package com.example.archtst.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserRoleModel {

    @EqualsAndHashCode.Include
    private UUID id;

    private UserModel user;
    private RoleModel role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserRoleModel(UserModel user, RoleModel role) {
        this.user = user;
        this.role = role;
    }
}