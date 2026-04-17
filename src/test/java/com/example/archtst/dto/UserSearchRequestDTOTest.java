package com.example.archtst.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserSearchRequestDTOTest {

    private static ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeAll
    static void setUpValidatorFactory() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void closeValidatorFactory() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        validator = validatorFactory.getValidator();
    }

    @Test
    void minAge_ShouldFailValidation_WhenNegative() {
        // Устанавливаем некорректный возраст (отрицательный)
        UserSearchRequestDTO dto = new UserSearchRequestDTO();
        dto.setMinAge(-5);

        // Запускаем валидацию
        Set<ConstraintViolation<UserSearchRequestDTO>> violations = validator.validate(dto);

        // Проверяем, что ошибка действительно появилась
        assertFalse(violations.isEmpty(), "Должна сработать ошибка валидации");

        // Проверяем, что текст ошибки совпадает с тем, что ты написал в аннотации
        ConstraintViolation<UserSearchRequestDTO> violation = violations.iterator().next();
        assertEquals("Минимальный возраст не может быть отрицательным", violation.getMessage());
    }

    @Test
    void minAge_ShouldPassValidation_WhenZeroOrPositive() {
        // Устанавливаем корректный возраст (0 или больше)
        UserSearchRequestDTO dto = new UserSearchRequestDTO();
        dto.setMinAge(0);

        Set<ConstraintViolation<UserSearchRequestDTO>> violationsZero = validator.validate(dto);
        assertTrue(violationsZero.isEmpty(), "При 0 ошибок быть не должно");

        dto.setMinAge(25);
        Set<ConstraintViolation<UserSearchRequestDTO>> violationsPositive = validator.validate(dto);
        assertTrue(violationsPositive.isEmpty(), "При положительном возрасте ошибок быть не должно");
    }
}