package com.example.archtst.persistence.mapper;

import com.example.archtst.model.UserRoleModel;
import com.example.archtst.persistence.entity.UserRole;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserRoleEntityMapper {

    private final ModelMapper modelMapper;

    public UserRoleEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserRoleModel toModel(UserRole entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, UserRoleModel.class);
    }

    public UserRole toEntity(UserRoleModel model) {
        if (model == null) return null;
        return modelMapper.map(model, UserRole.class);
    }
}