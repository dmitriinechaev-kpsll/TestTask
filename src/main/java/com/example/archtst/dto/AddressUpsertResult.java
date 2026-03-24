package com.example.archtst.dto;

public record AddressUpsertResult(
    AddressResponseDTO address,
    boolean isCreated // true - если создали, false - если обновили
){}
