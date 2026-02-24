package com.example.archtst.dto;

import com.example.archtst.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor  // Обязательно для ModelMapper
@AllArgsConstructor // Для Builder (чтобы работал конструктор со всеми полями)
public class ResponseDTO {
    private String id;
    private String name;
    private String email;
    private Integer age;
    private Integer shoeSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}