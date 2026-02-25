package com.example.archtst.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequestDTO(
        @NotBlank(message = "Город не может быть пустым")
        String city,

        @NotBlank(message = "Улица не может быть пустой")
        String street,

        @NotBlank(message = "Номер дома обязателен")
        String houseNumber
) {}