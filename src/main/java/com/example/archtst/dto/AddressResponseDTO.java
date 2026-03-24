package com.example.archtst.dto;

import com.example.archtst.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO{
        UUID   id;
        String city;
        String street;
        String houseNumber;
        AddressType type;
}
