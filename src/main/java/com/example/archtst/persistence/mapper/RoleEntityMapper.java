package com.example.archtst.persistence.mapper;

import com.example.archtst.model.RoleModel;
import com.example.archtst.persistence.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleEntityMapper {

    private final ModelMapper modelMapper;

    public RoleEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RoleModel toModel(Role entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, RoleModel.class);
    }

    public Role toEntity(RoleModel model) {
        if (model == null) return null;
        return modelMapper.map(model, Role.class);
    }
}