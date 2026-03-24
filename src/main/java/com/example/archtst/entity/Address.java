package com.example.archtst.entity;

import com.example.archtst.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "addresses")
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