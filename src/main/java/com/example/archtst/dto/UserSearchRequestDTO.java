package com.example.archtst.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект для фильтрации и пагинации пользователей")
public class UserSearchRequestDTO { // reqFilter

    @Schema(description = "Частичное совпадение по email", example = "vasya@example.com")
    private String  email;

    @Schema(description = "Список имен для поиска", example = "[\"Вася\", \"Иван\"]")
    private Set<String> names;
    //private Integer olderThan;  // перешли на minAge и maxAge

    @Schema(description = "Минимальный возраст", example = "18")
    @PositiveOrZero(message = "Минимальный возраст не может быть отрицательным")
    private Integer minAge;

    @Schema(description = "Максимальный возраст", example = "65")
    @PositiveOrZero(message = "Максимальный возраст не может быть отрицательным")
    private Integer maxAge;

    @Schema(description = "Номер страницы (начинается с 0)", example = "0", defaultValue = "0")
    @Min(value = 0, message = "Номер страницы не может быть меньше 0")
    private int page = 0;

    @Schema(description = "Количество записей на странице", example = "20", defaultValue = "20")
    @Min(value = 1, message = "Размер страницы должен быть минимум 1")
    @Max(value = 100, message = "Запрашивать больше 100 записей за раз нельзя")
    private int size = 20;

    @Schema(description = "Поле для сортировки (например: id, name, createdAt)", example = "createdAt")
    private String sortBy   = "name";

    @Schema(description = "Направление сортировки (ASC или DESC)", example = "DESC")
    @Pattern(regexp = "^(ASC|DESC)$", message = "Направление сортировки может быть только ASC или DESC")
    private String sortDir  = "ASC";
}
