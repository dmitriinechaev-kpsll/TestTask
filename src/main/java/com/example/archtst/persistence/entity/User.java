package com.example.archtst.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted = true, updated_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("deleted = false")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String email;

    private Integer age;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private boolean deleted = false;

    @Column(name = "shoe_size")
    private Integer shoeSize;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Address> addresses = new ArrayList<>();

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    public void addRole(Role role) {
        UserRole userRole = new UserRole(this, role);
        this.userRoles.add(userRole);
    }

    public void removeRole(Role role) {
        this.userRoles.removeIf(ur -> ur.getRole().equals(role));
    }
}