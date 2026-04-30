package com.example.archtst.mapper;

import com.example.archtst.dto.AddressRequestDTO;
import com.example.archtst.dto.AddressResponseDTO;
import com.example.archtst.model.AddressModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoMapper {

    private final ModelMapper modelMapper;

    public AddressDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AddressModel toModel(AddressRequestDTO request) {
        if (request == null) return null;
        return modelMapper.map(request, AddressModel.class);
    }

    public AddressResponseDTO toResponseDTO(AddressModel model) {
        if (model == null) return null;
        return modelMapper.map(model, AddressResponseDTO.class);
    }
}