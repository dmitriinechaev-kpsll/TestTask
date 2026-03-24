package com.example.archtst.mapper;

import com.example.archtst.dto.AddressRequestDTO;
import com.example.archtst.dto.AddressResponseDTO;
import com.example.archtst.entity.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private final ModelMapper modelMapper;

    public AddressMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // RequestDTO -> Entity
    // ModelMapper сам сопоставит поля city, street, houseNumber
    public Address toEntity(AddressRequestDTO request) {
        return modelMapper.map(request, Address.class);
    }

    // Entity -> ResponseDTO
    public AddressResponseDTO toResponseDTO(Address address) {
        return modelMapper.map(address, AddressResponseDTO.class);
    }
}
