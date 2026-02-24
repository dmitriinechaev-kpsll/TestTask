package com.example.archtst.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RequestDTO {

    @NotBlank(message = "Имя обязательно")
    @Size(min = 10, max = 100, message = "Имя должно быть от 10 до 100 символов")
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    private String email;

    private Integer shoeSize;

    @Min(value = 0, message = "Возраст должен быть положительным")
    @Max(value = 150, message = "Возраст не может превышать 150 лет")
    private Integer age;
}