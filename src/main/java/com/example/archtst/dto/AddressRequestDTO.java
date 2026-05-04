package com.example.archtst.dto;

import com.example.archtst.enums.AddressType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO{
        @Schema(description = "Город", example = "Кудрово")
        @NotBlank(message = "Город не может быть пустым")
        private String city;

        @Schema(description = "Улица", example = "ул. Кукушкина-Колотушкина")
        @NotBlank(message = "Улица не может быть пустой")
        private String street;

        @Schema(description = "Номер дома", example = "10")
        @NotBlank(message = "Номер дома обязателен")
        private String houseNumber;

        @Schema(description = "Тип адреса (HOME, WORK и т.д.)", example = "HOME")
        private AddressType type = AddressType.HOME;
}