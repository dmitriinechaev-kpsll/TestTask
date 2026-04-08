package com.example.archtst.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
// Включаем Soft Delete
//@SQLDelete(sql = "UPDATE user_roles SET deleted = true WHERE id=?")
@SQLDelete(sql = "UPDATE user_roles SET deleted = true, updated_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction("deleted = false")
public class UserRole {

    @Id
    @GeneratedValue
    private UUID id;

    // Ссылка на пользователя
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Ссылка на роль
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // --- НАШИ НОВЫЕ ПОЛЯ АУДИТА ---

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // ... конструкторы, геттеры и сеттеры ...

    //public UserRole() {}

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
    }
}
