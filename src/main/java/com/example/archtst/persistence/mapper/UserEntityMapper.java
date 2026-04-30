package com.example.archtst.persistence.mapper;

import com.example.archtst.model.UserModel;
import com.example.archtst.persistence.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    private final ModelMapper modelMapper;

    public UserEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserModel toModel(User entity) {
        if (entity == null) return null;
        UserModel model = modelMapper.map(entity, UserModel.class);
        return model;
    }

    public User toEntity(UserModel model) {
        if (model == null) return null;
        return modelMapper.map(model, User.class);
    }
}