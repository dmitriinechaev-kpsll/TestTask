package com.example.archtst.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
// 1. Подменяем физическое удаление на обновление флага
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ?")
// 2. Скрываем удаленных пользователей из всех выборок (findAll, findById и т.д.)
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

    // @Column(nullable = false, length = 100)
    private String name;

    // @Column(unique = true, nullable = false, length = 100)
    private String email;

    private Integer age;

    //@Column(name = "created_at", updatable = false)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    //@Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "shoe_size")
    private Integer shoeSize;

    // --- НОВАЯ СВЯЗЬ ---
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "user", // Ссылаемся на поле "user" в классе Address
            cascade = CascadeType.ALL, // Если сохраняем Юзера, сохраняются и адреса
            orphanRemoval = true // Если удалить адрес из списка, он удалится из базы
    )

    // Инициализируем пустым списком, чтобы не ловить NullPointerException
    private List<Address> addresses = new ArrayList<>();

    // ... методы helper'ы (опционально) ...

    public void addAddress(Address address) {
        addresses.add(address);
        address.setUser(this);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setUser(null);
    }

}