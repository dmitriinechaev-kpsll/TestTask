package com.example.archtst.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private int status;                 // HTTP статус (например, 404)
    private String message;             // Человекочитаемое сообщение ("Пользователь с ID 123 не найден")
    private LocalDateTime timestamp;    // Время, когда произошла ошибка
}
