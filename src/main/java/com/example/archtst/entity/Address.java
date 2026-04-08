package com.example.archtst.entity;

import com.example.archtst.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "addresses")
// 1. Перехватываем команду DELETE и заменяем на UPDATE
//@SQLDelete(sql = "UPDATE addresses SET deleted = true WHERE id=?")
@SQLDelete(sql = "UPDATE addresses SET deleted = true, updated_at = CURRENT_TIMESTAMP WHERE id=?")
// 2. Автоматически фильтруем SELECT-запросы, чтобы не видеть удаленные адреса
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String city;
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // --- СВЯЗЬ ---
    @ManyToOne(fetch = FetchType.LAZY) // LAZY - грузим пользователя только по требованию
    @JoinColumn(name = "user_id") // Указываем имя колонки в таблице addresses
    private User user;

    // --- ТАЙМСТАМПЫ ---
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_type", nullable = false)
    private AddressType type;
}