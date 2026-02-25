package com.example.archtst.dto;

import java.util.UUID;

public record AddressResponseDTO(
        UUID   id,
        String city,
        String street,
        String houseNumber
) {}
