package com.example.archtst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor  // Обязательно для ModelMapper
@AllArgsConstructor // Для Builder (чтобы работал конструктор со всеми полями)
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private Integer age;
    private Integer shoeSize;
    private Set<String> roles; // Просто отдаем список названий ролей (String)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}