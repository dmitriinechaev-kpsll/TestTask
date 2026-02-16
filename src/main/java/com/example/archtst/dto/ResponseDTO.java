package com.example.archtst.dto;

import com.example.archtst.entity.User;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class ResponseDTO {
    private String id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String formattedCreatedAt;

  /*  public static ResponseDTO fromEntity(User user) {
        if (user == null) return null;

        ResponseDTO dto = new ResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setFormattedCreatedAt(user.getCreatedAt() != null ?
                user.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) : null);
        return dto;
    }

   */
}