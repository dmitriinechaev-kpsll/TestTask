package com.example.archtst.entity;

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

    // НОВАЯ СВЯЗЬ: Многие ко Многим
    @ManyToMany(fetch = FetchType.EAGER) // Или LAZY, зависит от архитектуры
    @JoinTable(
            name = "user_roles", // Имя промежуточной таблицы в БД
            joinColumns = @JoinColumn(name = "user_id"), // Колонка, которая смотрит на этот класс (User)
            inverseJoinColumns = @JoinColumn(name = "role_id") // Колонка, которая смотрит на другой класс (Role)
    )
    // В ManyToMany лучше использовать Set (Множество), а не List,
    // чтобы Hibernate работал эффективнее и не допускал дубликатов.
    private Set<Role> roles = new HashSet<>();

    // Вспомогательные методы
    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }
}