package com.example.archtst.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserModel {

    @EqualsAndHashCode.Include
    private UUID id;

    private String name;
    private String email;
    private Integer age;
    private Integer shoeSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    private List<AddressModel> addresses = new ArrayList<>();

    @Builder.Default
    private Set<UserRoleModel> userRoles = new HashSet<>();

    public void addRole(RoleModel role) {
        UserRoleModel userRole = new UserRoleModel(this, role);
        this.userRoles.add(userRole);
    }

    public void removeRole(RoleModel role) {
        this.userRoles.removeIf(ur -> ur.getRole().equals(role));
    }

    public void addAddress(AddressModel address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(AddressModel address) {
        addresses.remove(address);
        address.setUser(null);
    }
}