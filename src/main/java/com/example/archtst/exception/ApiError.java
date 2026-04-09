package com.example.archtst.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private int status;                 // HTTP status (for example: 404)
    private String message;             // Human readable message ("Пользователь с ID 123 не найден")
    private LocalDateTime timestamp;    // Error time
}
