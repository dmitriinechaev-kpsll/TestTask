package com.example.archtst.persistence.mapper;

import com.example.archtst.model.AddressModel;
import com.example.archtst.persistence.entity.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressEntityMapper {

    private final ModelMapper modelMapper;

    public AddressEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AddressModel toModel(Address entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, AddressModel.class);
    }

    public Address toEntity(AddressModel model) {
        if (model == null) return null;
        return modelMapper.map(model, Address.class);
    }
}