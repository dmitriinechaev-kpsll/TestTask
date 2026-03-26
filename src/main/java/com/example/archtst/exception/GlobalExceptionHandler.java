package com.example.archtst.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Указываем, какую именно ошибку ловить
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("Ошибка поиска: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        log.warn("Ошибка создания: {}", ex.getMessage());
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    // Ловим именно ошибку валидации (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {

        // Собираем карту: "поле" -> "текст ошибки"
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Формируем красивый ответ
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Ошибка валидации данных");
        response.put("errors", errors); // Вставляем наш список ошибок

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiError> handlePropertyReferenceException(PropertyReferenceException ex) {

        // Формируем понятное сообщение.
        // ex.getPropertyName() вернет то самое кривое слово, например "string" или "abracadabra"
        String errorMessage = "Неверное поле для сортировки. Поле '" + ex.getPropertyName() + "' не найдено.";

        // Создаем твой объект ошибки (передаем статус 400, сообщение и текущее время)
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                LocalDateTime.now()
        );

        // Возвращаем красивый JSON со статусом 400 Bad Request
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        // Базовое сообщение, если мы просто отправили кривой JSON (например, забыли закрыть скобку)
        String errorMessage = "Некорректный формат JSON или переданы неверные типы данных.";

        // Пытаемся вытащить конкретную причину (например, ошибку Enum)
        Throwable cause = ex.getMostSpecificCause();
        if (cause instanceof InvalidFormatException) {
            // Берем только первую строчку из сообщения Jackson, чтобы не пугать клиента техническими деталями
            errorMessage = "Ошибка парсинга: " + cause.getMessage().split("\n")[0];
        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {

        // Создаем объект ошибки со статусом 404
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), // Берем сообщение из выброшенного исключения ("Адрес с ID ... не найден...")
                LocalDateTime.now()
        );

        // Возвращаем статус 404 Not Found
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiError);
    }
}
