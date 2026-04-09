package com.example.archtst.dto;

import com.example.archtst.entity.Address;

public record AddressUpsertResult(
    Address address,
    boolean isCreated // true - если создали, false - если обновили
){}
