package com.example.archtst.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 2, max = 100, message = "Имя должно быть от 2 до 100 символов")
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    private String email;

    @Min(value = 10, message = "Размер не может быть меньше 10")
    @Max(value = 60, message = "Размер не может быть больше 60")
    private Integer shoeSize;

    @Min(value = 0, message = "Возраст должен быть положительным")
    @Max(value = 150, message = "Возраст не может превышать 150 лет")
    private Integer age;
}