package com.example.archtst.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные для создания нового пользователя")
public class UserRequestDTO {

    @Schema(description = "Имя пользователя", example = "Иван Иванов")
    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    private String name;

    @Schema(description = "Электронная почта", example = "ivan@example.com")
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    private String email;

    @Schema(description = "Размер обуви", example = "42")
    @Min(value = 10, message = "Размер не может быть меньше 10")
    @Max(value = 60, message = "Размер не может быть больше 60")
    private Integer shoeSize;

    @Schema(description = "Возраст пользователя", example = "25")
    @Min(value = 0, message = "Возраст должен быть положительным")
    @Max(value = 120, message = "Возраст не может превышать 120 лет")
    private Integer age;
}