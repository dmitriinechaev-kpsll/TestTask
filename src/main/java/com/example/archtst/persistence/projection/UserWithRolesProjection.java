package com.example.archtst.persistence.projection;

import com.example.archtst.enums.RoleName;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface UserWithRolesProjection {
    UUID getId();
    String getName();
    String getEmail();
    Integer getAge();
    Integer getShoeSize();

    //    @Value("#{target.rolesString}")
    String getRolesString();

    default Set<RoleName> getRoles() {
        String rolesStr = getRolesString();
        if (rolesStr == null || rolesStr.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(rolesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(RoleName::valueOf)
                .collect(Collectors.toSet());
    }
}