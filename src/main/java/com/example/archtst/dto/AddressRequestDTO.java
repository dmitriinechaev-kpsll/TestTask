package com.example.archtst.dto;

import com.example.archtst.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDTO{
        @NotBlank(message = "Город не может быть пустым")
        String city;

        @NotBlank(message = "Улица не может быть пустой")
        String street;

        @NotBlank(message = "Номер дома обязателен")
        String houseNumber;

        private AddressType type = AddressType.HOME;
}