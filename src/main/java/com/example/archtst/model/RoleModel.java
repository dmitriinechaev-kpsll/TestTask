package com.example.archtst.model;

import com.example.archtst.enums.RoleName;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RoleModel {

    @EqualsAndHashCode.Include
    private UUID id;

    private RoleName name;
}