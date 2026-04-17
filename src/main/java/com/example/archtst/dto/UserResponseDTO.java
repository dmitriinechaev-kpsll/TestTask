package com.example.archtst.dto;

import com.example.archtst.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private Integer age;
    private Integer shoeSize;
    private Set<RoleName> roles = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}