package com.example.archtst.model;

import com.example.archtst.enums.AddressType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AddressModel {

    @EqualsAndHashCode.Include
    private UUID id;

    private String city;
    private String street;
    private String houseNumber;
    private AddressType type;
    private UserModel user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}